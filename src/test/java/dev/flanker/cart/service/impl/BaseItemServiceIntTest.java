package dev.flanker.cart.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.flanker.cart.ctx.CassandraConfiguration;
import dev.flanker.cart.ctx.ServiceConfiguration;
import dev.flanker.cart.db.cassandra.CassandraBindingRepository;
import dev.flanker.cart.db.cassandra.CassandraCartRepository;
import dev.flanker.cart.domain.Binding;
import dev.flanker.cart.domain.Item;
import dev.flanker.cart.util.RandomUtil;

@SpringBootTest(classes = {
        CassandraConfiguration.class,
        CassandraCartRepository.class,
        CassandraBindingRepository.class,
        BaseItemService.class,
        ServiceConfiguration.class })
class BaseItemServiceIntTest {
    @Autowired
    private CassandraBindingRepository bindingRepository;

    @Autowired
    private BaseItemService baseItemService;

    @Test
    public void okTest() {
        long userId = RandomUtil.userId();
        String itemId = RandomUtil.itemId();
        int number = RandomUtil.number();

        Item item = new Item(itemId, number);

        assertNull(baseItemService.get(userId, itemId).toCompletableFuture().join());

        baseItemService.put(userId, new Item(itemId, number)).toCompletableFuture().join();

        assertEquals(item, baseItemService.get(userId, itemId).toCompletableFuture().join());

        int incr = 3;

        Item increased = new Item(itemId, number + incr);
        assertEquals(increased, baseItemService.increase(userId, new Item(itemId, incr)).toCompletableFuture().join());
        assertEquals(increased, baseItemService.get(userId, itemId).toCompletableFuture().join());

        int decr = 2;
        Item decreased = new Item(itemId, increased.getNumber() - decr);
        assertEquals(decreased, baseItemService.decrease(userId, new Item(itemId, decr)).toCompletableFuture().join());
        assertEquals(decreased, baseItemService.get(userId, itemId).toCompletableFuture().join());

        baseItemService.delete(userId, itemId).toCompletableFuture().join();

        assertNull(baseItemService.get(userId, itemId).toCompletableFuture().join());

        bindingRepository.delete(userId).toCompletableFuture().join();
    }

    @Test
    public void notFoundTest() {
        Binding binding = RandomUtil.binding();
        String itemId = RandomUtil.itemId();

        bindingRepository.put(binding).toCompletableFuture().join();

        try {
            baseItemService.delete(binding.getUserId(), itemId).toCompletableFuture().join();
        } catch (Exception e) {
            // Expected not found
        }
        try {
            baseItemService.delete(binding.getUserId(), itemId).toCompletableFuture().join();
        } catch (Exception e) {
            // Expected not found
        }
        try {
            baseItemService.delete(binding.getUserId(), itemId).toCompletableFuture().join();
        } catch (Exception e) {
            // Expected not found
        }

        bindingRepository.delete(binding.getUserId()).toCompletableFuture().join();
    }

}
