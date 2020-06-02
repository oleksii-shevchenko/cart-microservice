package dev.flanker.cart.queue.gcp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.pubsub.v1.ReceivedMessage;

import dev.flanker.cart.component.SimpleConsumer;
import dev.flanker.cart.ctx.PubsubConfiguration;
import dev.flanker.cart.ctx.PubsubConsumerConfiguration;
import dev.flanker.cart.domain.Cart;
import dev.flanker.cart.generated.avro.Order;
import dev.flanker.cart.util.OrderUtil;
import dev.flanker.cart.util.RandomUtil;

@SpringBootTest(classes = {
        PubsubConfiguration.class,
        PubsubConsumerConfiguration.class,
        PubsubOrderQueue.class
})
@EnableConfigurationProperties
class PubsubOrderQueueIntTest {
    @Autowired
    private SimpleConsumer simpleConsumer;

    @Autowired
    private PubsubOrderQueue orderQueue;

    @Test
    public void okTest() throws Exception {
        long cartId = RandomUtil.cartId();
        Cart cart = RandomUtil.cart();

        String id = orderQueue.send(cartId, cart).toCompletableFuture().join();

        ReceivedMessage message = simpleConsumer.get();
        simpleConsumer.ack(message.getAckId());

        Order order = Order.fromByteBuffer(message.getMessage().getData().asReadOnlyByteBuffer());

        assertEquals(cart.getUserId(), order.getUserId());
        assertEquals(cartId, order.getCartId());
        assertEquals(cart.getItems().size(), order.getEntries().size());
        assertTrue(cart.getItems().contains(OrderUtil.toItem(order.getEntries().get(0))));
        assertTrue(cart.getItems().contains(OrderUtil.toItem(order.getEntries().get(1))));
    }
}
