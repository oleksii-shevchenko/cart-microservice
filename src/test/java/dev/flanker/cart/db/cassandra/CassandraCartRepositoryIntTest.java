package dev.flanker.cart.db.cassandra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.flanker.cart.ctx.CassandraConfiguration;
import dev.flanker.cart.domain.Item;


@SpringBootTest(classes = { CassandraConfiguration.class, CassandraCartRepository.class })
class CassandraCartRepositoryIntTest {
    private static int LARGE_PAGE_SIZE = 50;

    @Autowired
    private CassandraCartRepository cartRepository;

    @Test
    public void simpleOkTest() {
        long cartId = ThreadLocalRandom.current().nextLong();
        Item item = new Item("Java", 3);

        cartRepository.put(cartId, item).toCompletableFuture().join();
        assertEquals(item, cartRepository.get(cartId, item.getItemId()).toCompletableFuture().join());

        List<Item> items = cartRepository.get(cartId).toCompletableFuture().join();
        assertEquals(1, items.size());
        assertEquals(item, items.get(0));

        cartRepository.update(cartId, new Item("Java", 6)).toCompletableFuture().join();
        assertEquals(6, cartRepository.get(cartId, item.getItemId()).toCompletableFuture().join().getNumber());

        cartRepository.delete(cartId, item.getItemId()).toCompletableFuture().join();
        assertNull(cartRepository.get(cartId, item.getItemId()).toCompletableFuture().join());
        assertTrue(cartRepository.get(cartId).toCompletableFuture().join().isEmpty());
    }

    @Test
    public void largeItemsNumberTest() {
        long cartId = ThreadLocalRandom.current().nextLong();

        Set<Item> items = new HashSet<>();
        for (int i = 0; i < LARGE_PAGE_SIZE; i++) {
            Item item = new Item(UUID.randomUUID().toString(), ThreadLocalRandom.current().nextInt(10));
            items.add(item);
            cartRepository.put(cartId, item).toCompletableFuture().join();
        }

        assertEquals(items, new HashSet<>(cartRepository.get(cartId).toCompletableFuture().join()));
        cartRepository.delete(cartId).toCompletableFuture().join();
        assertTrue(cartRepository.get(cartId).toCompletableFuture().join().isEmpty());
    }
}
