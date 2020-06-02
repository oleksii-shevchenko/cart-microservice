package dev.flanker.cart.db.cassandra;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Mockito;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.BoundStatementBuilder;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;

public final class CassandraUtil {
    private CassandraUtil() {}

    public static Map<String, PreparedStatement> mockPreparedStatements(CqlSession session, List<String> statements) {
        Map<String, PreparedStatement> preparedStatements = new HashMap<>();
        for (String statement : statements) {
            PreparedStatement preparedStatement = mock(PreparedStatement.class);
            when(session.prepare(statement)).thenReturn(preparedStatement);
            preparedStatements.put(statement, preparedStatement);
        }
        return preparedStatements;
    }

    public static Map<String, BoundStatementBuilder> mockBuilders(Map<String, PreparedStatement> preparedStatements) {
        Map<String, BoundStatementBuilder> builders = new HashMap<>();
        for (Map.Entry<String, PreparedStatement> entry : preparedStatements.entrySet()) {
            BoundStatementBuilder builder = mock(BoundStatementBuilder.class, Mockito.RETURNS_SELF);
            when(entry.getValue().boundStatementBuilder(any())).thenReturn(builder);
            builders.put(entry.getKey(), builder);
        }
        return builders;
    }

    public static Map<String, BoundStatement> mockBoundStatements(Map<String, BoundStatementBuilder> builders) {
        Map<String, BoundStatement> boundStatements = new HashMap<>();
        for (Map.Entry<String, BoundStatementBuilder> entry : builders.entrySet()) {
            BoundStatement boundStatement = mock(BoundStatement.class);
            when(entry.getValue().build()).thenReturn(boundStatement);
            boundStatements.put(entry.getKey(), boundStatement);
        }
        return boundStatements;
    }
}
