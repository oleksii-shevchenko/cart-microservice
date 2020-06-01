package dev.flanker.cart.db.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import dev.flanker.cart.domain.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.failedFuture;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CassandraCartRepositoryTest {
    private CassandraCartRepository cartRepository;

    private CqlSession cqlSession;

    private Map<String, PreparedStatement> preparedStatements;

    private Map<String, BoundStatementBuilder> builders;

    private Map<String, BoundStatement> boundStatements;

    @BeforeEach
    public void prepareMocks() {
        List<String> statements = List.of(
                CassandraCartRepository.DELETE_CART_CQL,
                CassandraCartRepository.DELETE_ITEM_CQL,
                CassandraCartRepository.GET_CART_CQL,
                CassandraCartRepository.GET_ITEM_CQL,
                CassandraCartRepository.PUT_ITEM_CQL,
                CassandraCartRepository.UPDATE_ITEM_CQL
        );

        cqlSession = mock(CqlSession.class);
        preparedStatements = CassandraUtil.mockPreparedStatements(cqlSession, statements);
        builders = CassandraUtil.mockBuilders(preparedStatements);
        boundStatements = CassandraUtil.mockBoundStatements(builders);
        cartRepository = new CassandraCartRepository(cqlSession);
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
        String statement = CassandraCartRepository.PUT_ITEM_CQL;

        long cartId = 880;
        Item item = new Item("566", 3);

        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(completedFuture(null));

        cartRepository.put(cartId, item).toCompletableFuture().join();

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("cart_id", cartId);
        verify(builder).setString("item_id", item.getItemId());
        verify(builder).setInt("number", item.getNumber());
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
    }

    @Test
    public void failOkTest() {
        String statement = CassandraCartRepository.PUT_ITEM_CQL;

        long cartId = 880;
        Item item = new Item("566", 3);

        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(failedFuture(new RuntimeException()));

        try {
            cartRepository.put(cartId, item).toCompletableFuture().join();
            fail();
        } catch (Exception e) {
            // Expected exception
        }

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("cart_id", cartId);
        verify(builder).setString("item_id", item.getItemId());
        verify(builder).setInt("number", item.getNumber());
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
    }

    @Test
    public void getItemOkTest() {
        String statement = CassandraCartRepository.GET_ITEM_CQL;

        long cartId = 880;
        Item expected = new Item("566", 3);

        AsyncResultSet resultSet = mock(AsyncResultSet.class);
        Row row = mock(Row.class);

        when(resultSet.one()).thenReturn(row);
        when(row.getInt("number")).thenReturn(expected.getNumber());
        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(completedFuture(resultSet));

        Item actual = cartRepository.get(cartId, expected.getItemId()).toCompletableFuture().join();

        assertEquals(expected, actual);

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("cart_id", cartId);
        verify(builder).setString("item_id", expected.getItemId());
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
        verify(resultSet).one();
        verify(row).getInt("number");

        verifyNoMoreInteractions(resultSet, row);
    }

    @Test
    public void getItemNotFoundTest() {
        String statement = CassandraCartRepository.GET_ITEM_CQL;

        long cartId = 880;
        Item expected = new Item("566", 3);

        AsyncResultSet resultSet = mock(AsyncResultSet.class);

        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(completedFuture(resultSet));

        Item actual = cartRepository.get(cartId, expected.getItemId()).toCompletableFuture().join();

        assertNull(actual);

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("cart_id", cartId);
        verify(builder).setString("item_id", expected.getItemId());
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
        verify(resultSet).one();

        verifyNoMoreInteractions(resultSet);
    }

    @Test
    public void getItemFailTest() {
        String statement = CassandraCartRepository.GET_ITEM_CQL;

        long cartId = 880;
        Item expected = new Item("566", 3);

        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(failedFuture(new RuntimeException()));

        try {
            cartRepository.get(cartId, expected.getItemId()).toCompletableFuture().join();
        } catch (Exception e) {
            // Expected exception
        }

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("cart_id", cartId);
        verify(builder).setString("item_id", expected.getItemId());
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
    }

    @Test
    public void deleteCartOkTest() {
        String statement = CassandraCartRepository.DELETE_CART_CQL;

        long cartId = 880;

        AsyncResultSet resultSet = mock(AsyncResultSet.class);

        when(resultSet.wasApplied()).thenReturn(true);
        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(completedFuture(resultSet));

        Boolean deleted = cartRepository.delete(cartId).toCompletableFuture().join();

        assertTrue(deleted);

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("cart_id", cartId);
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
        verify(resultSet).wasApplied();

        verifyNoMoreInteractions(resultSet);
    }

    @Test
    public void deleteCartFailTest() {
        String statement = CassandraCartRepository.DELETE_CART_CQL;

        long cartId = 880;

        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(failedFuture(new RuntimeException()));

        try {
            cartRepository.delete(cartId).toCompletableFuture().join();
        } catch (Exception e) {
            // Expected exception
        }

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("cart_id", cartId);
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
    }

    @Test
    public void deleteItemOkTest() {
        String statement = CassandraCartRepository.DELETE_ITEM_CQL;

        long cartId = 880;
        String itemId = "12345";

        AsyncResultSet resultSet = mock(AsyncResultSet.class);

        when(resultSet.wasApplied()).thenReturn(true);
        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(completedFuture(resultSet));

        Boolean deleted = cartRepository.delete(cartId, itemId).toCompletableFuture().join();

        assertTrue(deleted);

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("cart_id", cartId);
        verify(builder).setString("item_id", itemId);
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
        verify(resultSet).wasApplied();

        verifyNoMoreInteractions(resultSet);
    }

    @Test
    public void deleteItemFailTest() {
        String statement = CassandraCartRepository.DELETE_ITEM_CQL;

        long cartId = 880;
        String itemId = "21345";

        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(failedFuture(new RuntimeException()));

        try {
            cartRepository.delete(cartId, itemId).toCompletableFuture().join();
        } catch (Exception e) {
            // Expected exception
        }

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("cart_id", cartId);
        verify(builder).setString("item_id", itemId);
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
    }

    @Test
    public void updateItemOkTest() {
        String statement = CassandraCartRepository.UPDATE_ITEM_CQL;

        long cartId = 880;
        String itemId = "12345";
        int number = 5;
        Item item = new Item(itemId, number);

        AsyncResultSet resultSet = mock(AsyncResultSet.class);

        when(resultSet.wasApplied()).thenReturn(true);
        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(completedFuture(resultSet));

        Boolean deleted = cartRepository.update(cartId, item).toCompletableFuture().join();

        assertTrue(deleted);

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("cart_id", cartId);
        verify(builder).setString("item_id", itemId);
        verify(builder).setInt("number", number);
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
        verify(resultSet).wasApplied();

        verifyNoMoreInteractions(resultSet);
    }

    @Test
    public void updateItemFailTest() {
        String statement = CassandraCartRepository.UPDATE_ITEM_CQL;

        long cartId = 880;
        String itemId = "21345";
        int number = 5;
        Item item = new Item(itemId, number);

        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(failedFuture(new RuntimeException()));

        try {
            cartRepository.update(cartId, item).toCompletableFuture().join();
        } catch (Exception e) {
            // Expected exception
        }

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("cart_id", cartId);
        verify(builder).setString("item_id", itemId);
        verify(builder).setInt("number", number);
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
    }

    @Test
    public void getCartOkTest() {
        String statement = CassandraCartRepository.GET_CART_CQL;

        long cartId = 880;

        List<Item> firstPage = List.of(
                new Item("1234", 4),
                new Item("6784", 7),
                new Item("3335", 3)
        );

        List<Item> secondPage = List.of(
                new Item("699670", 88),
                new Item("36611", 34)
        );

        List<Row> firstPageRaw = firstPage.stream()
                .map(CassandraCartRepositoryTest::mockItem)
                .collect(Collectors.toList());

        List<Row> secondPageRaw = secondPage.stream()
                .map(CassandraCartRepositoryTest::mockItem)
                .collect(Collectors.toList());

        AsyncResultSet firstResultSet = mock(AsyncResultSet.class);
        when(firstResultSet.hasMorePages()).thenReturn(true);
        when(firstResultSet.currentPage()).thenReturn(firstPageRaw);

        AsyncResultSet secondResultSet = mock(AsyncResultSet.class);
        when(secondResultSet.hasMorePages()).thenReturn(false);
        when(secondResultSet.currentPage()).thenReturn(secondPageRaw);

        when(firstResultSet.fetchNextPage()).thenReturn(completedFuture(secondResultSet));

        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(completedFuture(firstResultSet));

        Set<Item> actual = new HashSet<>(cartRepository.get(cartId).toCompletableFuture().join());

        Set<Item> expected = new HashSet<>();
        expected.addAll(firstPage);
        expected.addAll(secondPage);

        assertEquals(expected, actual);

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("cart_id", cartId);
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();

        verify(firstResultSet).hasMorePages();
        verify(firstResultSet).fetchNextPage();
        verify(firstResultSet).currentPage();
        verify(firstResultSet).remaining();

        verify(secondResultSet).currentPage();
        verify(secondResultSet).hasMorePages();
        verify(secondResultSet).remaining();

        verifyNoMoreInteractions(firstResultSet, secondResultSet);
    }

    @Test
    public void getCartFailTest() {
        String statement = CassandraCartRepository.GET_CART_CQL;

        long cartId = 880;

        when(cqlSession.executeAsync(boundStatements.get(statement))).thenReturn(failedFuture(new RuntimeException()));

        try {
            cartRepository.get(cartId).toCompletableFuture().join();
        } catch (Exception e) {
            // Expected exception
        }

        BoundStatementBuilder builder = builders.get(statement);
        verify(builder).setLong("cart_id", cartId);
        verify(builder).build();
        verify(cqlSession).executeAsync(eq(boundStatements.get(statement)));
        verify(preparedStatements.get(statement)).boundStatementBuilder();
    }

    private static Row mockItem(Item item) {
        Row row = mock(Row.class);
        when(row.getString("item_id")).thenReturn(item.getItemId());
        when(row.getInt("number")).thenReturn(item.getNumber());
        return row;
    }
}