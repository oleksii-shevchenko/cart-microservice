package dev.flanker.cart.db.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.AsyncResultSet;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import dev.flanker.cart.db.BindingRepository;
import dev.flanker.cart.domain.Binding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletionStage;

@Repository
public class CassandraBindingRepository implements BindingRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraBindingRepository.class);

    static final String PUT_BINDING_CQL = "INSERT INTO user_binding (user_id, cart_id) VALUES (:user_id, :cart_id)";

    static final String GET_BINDING_CQL = "SELECT cart_id FROM user_binding WHERE user_id = :user_id";

    static final String DELETE_BINDING_CQL = "DELETE FROM user_binding WHERE user_id = :user_id";

    private final PreparedStatement putBindingStatement;

    private final PreparedStatement getBindingStatement;

    private final PreparedStatement deleteBindingStatement;

    private final CqlSession session;

    @Autowired
    public CassandraBindingRepository(CqlSession session) {
        this.session = session;
        this.putBindingStatement = session.prepare(PUT_BINDING_CQL);
        this.getBindingStatement = session.prepare(GET_BINDING_CQL);
        this.deleteBindingStatement = session.prepare(DELETE_BINDING_CQL);
    }

    @Override
    public CompletionStage<Void> put(Binding binding) {
        BoundStatement boundStatement = putBindingStatement.boundStatementBuilder()
                .setLong("user_id", binding.getUserId())
                .setLong("cart_id", binding.getCartId())
                .build();
        LOGGER.info("Prepared binding insert [userId={}, cartId={}].", binding.getUserId(), binding.getCartId());
        return session.executeAsync(boundStatement)
                .thenAccept(rs -> {});
    }

    @Override
    public CompletionStage<Binding> get(long userId) {
        BoundStatement boundStatement = getBindingStatement.boundStatementBuilder()
                .setLong("user_id", userId)
                .build();
        return session.executeAsync(boundStatement)
                .thenApply(rs -> {
                    Row row = rs.one();
                    if (row != null) {
                        return new Binding(userId, row.getLong("cart_id"));
                    } else {
                        return null;
                    }
                });
    }

    @Override
    public CompletionStage<Boolean> delete(long userId) {
        BoundStatement boundStatement = deleteBindingStatement.boundStatementBuilder()
                .setLong("user_id", userId)
                .build();
        LOGGER.info("Prepared binding delete [userId={}].", userId);
        return session.executeAsync(boundStatement)
                .thenApply(AsyncResultSet::wasApplied);
    }
}
