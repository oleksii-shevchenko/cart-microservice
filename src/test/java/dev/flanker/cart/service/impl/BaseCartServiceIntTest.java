package dev.flanker.cart.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import dev.flanker.cart.ctx.CassandraConfiguration;
import dev.flanker.cart.db.cassandra.CassandraBindingRepository;
import dev.flanker.cart.db.cassandra.CassandraCartRepository;
import dev.flanker.cart.domain.Binding;
import dev.flanker.cart.domain.Item;
import dev.flanker.cart.util.RandomUtil;

@SpringBootTest(classes = {
        CassandraConfiguration.class,
        CassandraCartRepository.class,
        CassandraBindingRepository.class,
        BaseCartService.class})
@TestPropertySource(locations = "classpath:application.properties")
@EnableConfigurationProperties
class BaseCartServiceIntTest {
    @Autowired
    private CassandraCartRepository cartRepository;

    @Autowired
    private CassandraBindingRepository bindingRepository;

    @Autowired
    private BaseCartService baseCartService;

    @Test
    public void okTest() {
        Binding binding = RandomUtil.binding();

        assertTrue(baseCartService.get(binding.getUserId()).toCompletableFuture().join().getItems().isEmpty());

        bindingRepository.put(binding).toCompletableFuture().join();

        assertTrue(baseCartService.get(binding.getUserId()).toCompletableFuture().join().getItems().isEmpty());

        List<Item> items = RandomUtil.cart().getItems();
        for (Item item : items) {
            cartRepository.put(binding.getCartId(), item).toCompletableFuture().join();
        }

        assertEquals(new HashSet<>(items), new HashSet<>(baseCartService.get(binding.getUserId()).toCompletableFuture().join().getItems()));

        baseCartService.delete(binding.getUserId()).toCompletableFuture().join();

        assertTrue(baseCartService.get(binding.getUserId()).toCompletableFuture().join().getItems().isEmpty());

        bindingRepository.delete(binding.getUserId()).toCompletableFuture().join();

        assertTrue(baseCartService.get(binding.getUserId()).toCompletableFuture().join().getItems().isEmpty());
    }

}
