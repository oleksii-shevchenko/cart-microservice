package dev.flanker.cart.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.google.pubsub.v1.ReceivedMessage;

import dev.flanker.cart.component.SimpleConsumer;
import dev.flanker.cart.ctx.CassandraConfiguration;
import dev.flanker.cart.ctx.PubsubConfiguration;
import dev.flanker.cart.ctx.PubsubConsumerConfiguration;
import dev.flanker.cart.db.cassandra.CassandraBindingRepository;
import dev.flanker.cart.db.cassandra.CassandraCartRepository;
import dev.flanker.cart.domain.Binding;
import dev.flanker.cart.domain.Cart;
import dev.flanker.cart.domain.Item;
import dev.flanker.cart.generated.avro.Order;
import dev.flanker.cart.generated.avro.OrderEntry;
import dev.flanker.cart.queue.gcp.PubsubOrderQueue;
import dev.flanker.cart.util.RandomUtil;

@SpringBootTest(classes = {
        CassandraConfiguration.class,
        CassandraCartRepository.class,
        CassandraBindingRepository.class,
        PubsubConfiguration.class,
        PubsubConsumerConfiguration.class,
        PubsubOrderQueue.class,
        BaseOrderService.class })
@TestPropertySource(locations = "classpath:application.properties")
@EnableConfigurationProperties
class BaseOrderServiceIntTest {
    @Autowired
    private CassandraCartRepository cartRepository;

    @Autowired
    private CassandraBindingRepository bindingRepository;

    @Autowired
    private SimpleConsumer simpleConsumer;

    @Autowired
    private BaseOrderService baseOrderService;

    @Test
    public void okTest() throws Exception {
        Binding binding = RandomUtil.binding();
        Cart cart = RandomUtil.cart();
        cart.setUserId(binding.getUserId());

        bindingRepository.put(binding).toCompletableFuture().join();
        for (Item item : cart.getItems()) {
            cartRepository.put(binding.getCartId(), item).toCompletableFuture().join();
        }

        Cart completedCart = baseOrderService.completeOrder(binding.getUserId()).toCompletableFuture().join();
        assertEquals(cart.getUserId(), completedCart.getUserId());
        assertEquals(new HashSet<>(cart.getItems()), new HashSet<>(completedCart.getItems()));

        ReceivedMessage receivedMessage = simpleConsumer.get();
        simpleConsumer.ack(receivedMessage.getAckId());

        Order order = Order.fromByteBuffer(receivedMessage.getMessage().getData().asReadOnlyByteBuffer());
        assertEquals(binding.getUserId(), order.getUserId());
        assertEquals(binding.getCartId(), order.getCartId());
        assertEquals(cart.getItems().size(), order.getEntries().size());
        for (OrderEntry entry : order.getEntries()) {
            assertTrue(cart.getItems().contains(toItem(entry)));
        }
        assertNull(bindingRepository.get(binding.getUserId()).toCompletableFuture().join());
        assertTrue(cartRepository.get(binding.getCartId()).toCompletableFuture().join().isEmpty());
    }


    @Test
    public void failTest() {
        Binding binding = RandomUtil.binding();

        try {
            baseOrderService.completeOrder(binding.getUserId()).toCompletableFuture().join();
        } catch (Exception e) {
            // Expected not found
        }
        bindingRepository.put(binding).toCompletableFuture().join();
        try {
            baseOrderService.completeOrder(binding.getUserId()).toCompletableFuture().join();
        } catch (Exception e) {
            // Cart cannot be empty
        }
        bindingRepository.delete(binding.getUserId()).toCompletableFuture().join();
    }

    private static Item toItem(OrderEntry entry) {
        return new Item(entry.getItemId(), entry.getNumber());
    }
}
