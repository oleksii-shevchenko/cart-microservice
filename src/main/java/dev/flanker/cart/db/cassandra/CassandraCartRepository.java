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

    static final String PUT_ITEM_CQL = "INSERT INTO cart (cartId, itemId, number) VALUES (:cartId, :itemId, :number)";

    static final String GET_ITEM_CQL = "SELECT number FROM cart WHERE cartId = :cartId AND itemId = :itemId";

    static final String GET_CART_CQL = "SELECT itemId, number FROM cart WHERE cartId = :cartId";

    static final String DELETE_CART_CQL = "DELETE FROM cart WHERE cartId = :cartId";

    static final String DELETE_ITEM_CQL = "DELETE FROM cart WHERE cartId = :cartId AND itemId = :itemId";

    static final String UPDATE_ITEM_CQL =
            "UPDATE cart SET number = number + :diff WHERE cartId = :cartId AND itemId = :itemId IF number + :diff > 0";

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
                .setLong("cartId", cartId)
                .setString("itemId", item.getItemId())
                .setInt("number", item.getNumber())
                .build();
        LOGGER.info("Prepared item insert [cartId={}, itemId={}].", cartId, item.getItemId());
        return session.executeAsync(boundStatement)
                .thenAccept(rs -> {});
    }

    @Override
    public CompletionStage<Item> get(long cartId, String itemId) {
        BoundStatement boundStatement = getItemStatement.boundStatementBuilder()
                .setLong("cartId", cartId)
                .setString("itemId", itemId)
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
                .setLong("cartId", cartId)
                .build();
        return session.executeAsync(boundStatement)
                .thenCompose(rs -> composePages(rs, new ArrayList<>()));
    }

    @Override
    public CompletionStage<Boolean> delete(long cartId) {
        BoundStatement boundStatement = deleteCartStatement.boundStatementBuilder()
                .setLong("cartId", cartId)
                .build();
        LOGGER.info("Prepared a cart delete [cartId={}].", cartId);
        return session.executeAsync(boundStatement)
                .thenApply(AsyncResultSet::wasApplied);
    }

    @Override
    public CompletionStage<Boolean> delete(long cartId, String itemId) {
        BoundStatement boundStatement = deleteItemStatement.boundStatementBuilder()
                .setLong("cartId", cartId)
                .setString("itemId", itemId)
                .build();
        LOGGER.info("Prepared item delete [cartId={}, itemId={}].", cartId, itemId);
        return session.executeAsync(boundStatement)
                .thenApply(AsyncResultSet::wasApplied);
    }

    @Override
    public CompletionStage<Boolean> update(long cartId, String itemId, int numberDifference) {
        BoundStatement boundStatement = updateItemStatement.boundStatementBuilder()
                .setLong("cartId", cartId)
                .setString("itemId", itemId)
                .setInt("diff", numberDifference)
                .build();
        LOGGER.info("Prepared item update [cartId={}, itemId={}].", cartId, itemId);
        return session.executeAsync(boundStatement)
                .thenApply(AsyncResultSet::wasApplied);
    }

    CompletionStage<List<Item>> composePages(AsyncResultSet resultSet, List<Item> accumulator) {
        synchronise = synchronise + 1; // Update accumulator from memory

        for (Row row : resultSet.currentPage()) {
            accumulator.add(new Item(row.getString("cartId"), row.getInt("number")));
        }

        synchronise = synchronise + 1; // Flush accumulator to memory

        if (resultSet.hasMorePages()) {
            return resultSet.fetchNextPage().thenCompose(rs -> composePages(rs, accumulator));
        } else {
            return CompletableFuture.completedFuture(accumulator);
        }

    }
}
