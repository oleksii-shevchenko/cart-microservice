package dev.flanker.cart.db.cassandra;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.failedFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.AsyncResultSet;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.BoundStatementBuilder;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;

import dev.flanker.cart.domain.Binding;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CassandraBindingRepositoryTest {
    private CassandraBindingRepository bindingRepository;

    private CqlSession cqlSession;

    private Map<String, PreparedStatement> preparedStatements;

    private Map<String, BoundStatementBuilder> builders;

    private Map<String, BoundStatement> boundStatements;

    @BeforeEach
    public void prepareMocks() {
        List<String> statements = List.of(
                CassandraBindingRepository.GET_BINDING_CQL,
                CassandraBindingRepository.PUT_BINDING_CQL,
                CassandraBindingRepository.DELETE_BINDING_CQL
        );

        cqlSession = mock(CqlSession.class);
        preparedStatements = CassandraUtil.mockPreparedStatements(cqlSession, statements);
        builders = CassandraUtil.mockBuilders(preparedStatements);
        boundStatements = CassandraUtil.mockBoundStatements(builders);
        bindingRepository = new CassandraBindingRepository(cqlSession);
    }

    @AfterEach
    public void checkInteractions() {
        preparedStatements.keySet().forEach(statement -> verify(cqlSession).prepare(statement));
        verifyNoMoreInteractions(cqlSession);
        preparedStatements.values().forEach(Mockito::verifyNoMoreInteractions);
        builders.values().forEach(Mockito::verifyNoMoreInteractions);
        boundStatements.values().forEach(Mockito::verifyNoMoreInteractions);
    }

    @Test
    public void putOkTest() {
        String statement = CassandraBindingRepository.PUT_BINDING_CQL;
        Binding binding = new Binding(354354, 2134);

        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(completedFuture(null));

        bindingRepository.put(binding).toCompletableFuture().join();

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("user_id", binding.getUserId());
        verify(builder).setLong("cart_id", binding.getCartId());
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
    }

    @Test
    public void putFailTest() {
        String statement = CassandraBindingRepository.PUT_BINDING_CQL;
        Binding binding = new Binding(86754, 2134);

        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(failedFuture(new RuntimeException()));

        try {
            bindingRepository.put(binding).toCompletableFuture().join();
            fail();
        } catch (Exception e) {
            // Expected exception
        }

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("user_id", binding.getUserId());
        verify(builder).setLong("cart_id", binding.getCartId());
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
    }

    @Test
    public void getOkTest() {
        String statement = CassandraBindingRepository.GET_BINDING_CQL;
        Binding expected = new Binding(987987, 2134);

        AsyncResultSet resultSet = mock(AsyncResultSet.class);
        Row row = mock(Row.class);

        when(resultSet.one()).thenReturn(row);
        when(row.getLong("cart_id")).thenReturn(expected.getCartId());

        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(completedFuture(resultSet));

        Binding actual = bindingRepository.get(expected.getUserId()).toCompletableFuture().join();

        assertEquals(actual.getCartId(), expected.getCartId());
        assertEquals(actual.getUserId(), expected.getUserId());

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("user_id", expected.getUserId());
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
        verify(resultSet).one();
        verify(row).getLong("cart_id");

        verifyNoMoreInteractions(resultSet, row);
    }

    @Test
    public void getNotFoundTest() {
        String statement = CassandraBindingRepository.GET_BINDING_CQL;
        Binding expected = new Binding(45665, 2134);

        AsyncResultSet resultSet = mock(AsyncResultSet.class);

        when(resultSet.one()).thenReturn(null);

        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(completedFuture(resultSet));

        Binding actual = bindingRepository.get(expected.getUserId()).toCompletableFuture().join();

        assertNull(actual);

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("user_id", expected.getUserId());
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
        verify(resultSet).one();

        verifyNoMoreInteractions(resultSet);
    }

    @Test
    public void getFailTest() {
        String statement = CassandraBindingRepository.GET_BINDING_CQL;
        Binding expected = new Binding(678671, 2134);

        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(failedFuture(new RuntimeException()));

        try {
            bindingRepository.get(expected.getUserId()).toCompletableFuture().join();
            fail();
        } catch (Exception e) {
            // Expected exception
        }

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("user_id", expected.getUserId());
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
    }

    @Test
    public void deleteOkTest() {
        String statement = CassandraBindingRepository.DELETE_BINDING_CQL;
        Binding expected = new Binding(1234555, 2134);

        AsyncResultSet resultSet = mock(AsyncResultSet.class);

        when(resultSet.wasApplied()).thenReturn(true);
        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(completedFuture(resultSet));

        Boolean deleted = bindingRepository.delete(expected.getUserId()).toCompletableFuture().join();

        assertTrue(deleted);

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("user_id", expected.getUserId());
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
        verify(resultSet).wasApplied();

        verifyNoMoreInteractions(resultSet);
    }

    @Test
    public void deleteFailOkTest() {
        String statement = CassandraBindingRepository.DELETE_BINDING_CQL;
        Binding expected = new Binding(6786783, 2134);

        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(failedFuture(new RuntimeException()));

        try {
            bindingRepository.delete(expected.getUserId()).toCompletableFuture().join();
            fail();
        } catch (Exception e) {
            // Expected exception
        }

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("user_id", expected.getUserId());
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
    }
}
