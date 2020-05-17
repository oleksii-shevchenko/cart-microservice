package dev.flanker.cart.db.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import dev.flanker.cart.db.BindingRepository;
import dev.flanker.cart.rest.domain.Binding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletionStage;

public class CassandraBindingRepository implements BindingRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraBindingRepository.class);

    static final String PUT_BINDING_CQL = "INSERT INTO user_binding (user_id, cart_id) VALUES (:userId, :cartId)";

    static final String GET_BINDING_CQL = "SELECT cart_id FROM user_binding WHERE userId = :userId";

    static final String DELETE_BINDING_CQL = "DELETE FROM user_binding WHERE userId = :userId";


    private final PreparedStatement putBindingStatement;

    private final PreparedStatement getBindingStatement;

    private final PreparedStatement deleteBindingStatement;

    private final CqlSession session;

    public CassandraBindingRepository(CqlSession session) {
        this.session = session;
        this.putBindingStatement = session.prepare(PUT_BINDING_CQL);
        this.getBindingStatement = session.prepare(GET_BINDING_CQL);
        this.deleteBindingStatement = session.prepare(DELETE_BINDING_CQL);
    }

    @Override
    public CompletionStage<Void> put(Binding binding) {
        BoundStatement boundStatement = putBindingStatement.boundStatementBuilder()
                .setLong("userId", binding.getUserId())
                .setLong("cartId", binding.getCartId())
                .build();
        LOGGER.info("Prepared binding insert [userId={}, cartId={}]", binding.getUserId(), binding.getCartId());
        return session.executeAsync(boundStatement)
                .thenAccept(rs -> {});
    }

    @Override
    public CompletionStage<Binding> get(long userId) {
        BoundStatement boundStatement = getBindingStatement.boundStatementBuilder()
                .setLong("userId", userId)
                .build();
        return session.executeAsync(boundStatement)
                .thenApply(rs -> {
                    Row row = rs.one();
                    if (row != null) {
                        return new Binding(userId, row.getInt("cartId"));
                    } else {
                        return null;
                    }
                });
    }

    @Override
    public CompletionStage<Void> delete(long userId) {
        BoundStatement boundStatement = deleteBindingStatement.boundStatementBuilder()
                .setLong("userId", userId)
                .build();
        LOGGER.info("Prepared binding delete [userId={}]", userId);
        return session.executeAsync(boundStatement)
                .thenAccept(rs -> {});
    }
}
