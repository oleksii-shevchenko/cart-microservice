package dev.flanker.cart.db.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.AsyncResultSet;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import dev.flanker.cart.db.CartRepository;
import dev.flanker.cart.domain.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Repository
public class CassandraCartRepository implements CartRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraCartRepository.class);

    static final String PUT_ITEM_CQL = "INSERT INTO cart (cart_id, item_id, number) VALUES (:cart_id, :item_id, :number)";

    static final String GET_ITEM_CQL = "SELECT number FROM cart WHERE cart_id = :cart_id AND item_id = :item_id";

    static final String GET_CART_CQL = "SELECT item_id, number FROM cart WHERE cart_id = :cart_id";

    static final String DELETE_CART_CQL = "DELETE FROM cart WHERE cart_id = :cart_id";

    static final String DELETE_ITEM_CQL = "DELETE FROM cart WHERE cart_id = :cart_id AND item_id = :item_id";

    static final String UPDATE_ITEM_CQL = "UPDATE cart SET number = :number WHERE cart_id = :cart_id AND item_id = :item_id";

    private final PreparedStatement putItemStatement;

    private final PreparedStatement getItemStatement;

    private final PreparedStatement getCartStatement;

    private final PreparedStatement deleteCartStatement;

    private final PreparedStatement deleteItemStatement;

    private final PreparedStatement updateItemStatement;

    private final CqlSession session;

    private volatile int synchronise = 0;

    @Autowired
    public CassandraCartRepository(CqlSession session) {
        this.session = session;
        this.putItemStatement = session.prepare(PUT_ITEM_CQL);
        this.getItemStatement = session.prepare(GET_ITEM_CQL);
        this.getCartStatement = session.prepare(GET_CART_CQL);
        this.deleteItemStatement = session.prepare(DELETE_ITEM_CQL);
        this.deleteCartStatement = session.prepare(DELETE_CART_CQL);
        this.updateItemStatement = session.prepare(UPDATE_ITEM_CQL);
    }

    @Override
    public CompletionStage<Void> put(long cartId, Item item) {
        BoundStatement boundStatement = putItemStatement.boundStatementBuilder()
                .setLong("cart_id", cartId)
                .setString("item_id", item.getItemId())
                .setInt("number", item.getNumber())
                .build();
        LOGGER.info("Prepared item insert [cartId={}, itemId={}].", cartId, item.getItemId());
        return session.executeAsync(boundStatement)
                .thenAccept(rs -> {});
    }

    @Override
    public CompletionStage<Item> get(long cartId, String itemId) {
        BoundStatement boundStatement = getItemStatement.boundStatementBuilder()
                .setLong("cart_id", cartId)
                .setString("item_id", itemId)
                .build();
        return session.executeAsync(boundStatement)
                .thenApply(rs -> {
                    Row row = rs.one();
                    if (row != null) {
                        return new Item(itemId, row.getInt("number"));
                    } else {
                        return null;
                    }
                });
    }

    @Override
    public CompletionStage<List<Item>> get(long cartId) {
        BoundStatement boundStatement = getCartStatement.boundStatementBuilder()
                .setLong("cart_id", cartId)
                .build();
        LOGGER.info("Prepared cart get [cartId={}]", cartId);
        return session.executeAsync(boundStatement)
                .thenCompose(rs -> composePages(rs, new ArrayList<>()));
    }

    @Override
    public CompletionStage<Boolean> delete(long cartId) {
        BoundStatement boundStatement = deleteCartStatement.boundStatementBuilder()
                .setLong("cart_id", cartId)
                .build();
        LOGGER.info("Prepared a cart delete [cartId={}].", cartId);
        return session.executeAsync(boundStatement)
                .thenApply(AsyncResultSet::wasApplied);
    }

    @Override
    public CompletionStage<Boolean> delete(long cartId, String itemId) {
        BoundStatement boundStatement = deleteItemStatement.boundStatementBuilder()
                .setLong("cart_id", cartId)
                .setString("item_id", itemId)
                .build();
        LOGGER.info("Prepared item delete [cartId={}, itemId={}].", cartId, itemId);
        return session.executeAsync(boundStatement)
                .thenApply(AsyncResultSet::wasApplied);
    }

    @Override
    public CompletionStage<Boolean> update(long cartId, Item item) {
        BoundStatement boundStatement = updateItemStatement.boundStatementBuilder()
                .setLong("cart_id", cartId)
                .setString("item_id", item.getItemId())
                .setInt("number", item.getNumber())
                .build();
        LOGGER.info("Prepared item update [cartId={}, itemId={}].", cartId, item.getItemId());
        return session.executeAsync(boundStatement)
                .thenApply(AsyncResultSet::wasApplied);
    }

    private CompletionStage<List<Item>> composePages(AsyncResultSet resultSet, List<Item> accumulator) {
        synchronise = synchronise + 1; // Update accumulator from memory

        LOGGER.info("Start parsing page [size={}]", resultSet.remaining());
        for (Row row : resultSet.currentPage()) {
            accumulator.add(new Item(row.getString("item_id"), row.getInt("number")));
        }

        synchronise = synchronise + 1; // Flush accumulator to memory

        if (resultSet.hasMorePages()) {
            return resultSet.fetchNextPage().thenCompose(rs -> composePages(rs, accumulator));
        } else {
            LOGGER.info("Finished parsing cart [size={}]", accumulator.size());
            return CompletableFuture.completedFuture(accumulator);
        }

    }
}
