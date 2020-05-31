package dev.flanker.cart.queue.gcp;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.PublisherInterface;
import com.google.protobuf.UnsafeByteOperations;
import com.google.pubsub.v1.PubsubMessage;
import com.spotify.futures.ApiFuturesExtra;
import dev.flanker.cart.domain.Cart;
import dev.flanker.cart.queue.OrderQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ForkJoinPool;

@Component
public class PubsubOrderQueue implements OrderQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(PubsubOrderQueue.class);

    private final PublisherInterface publisher;

    @Autowired
    public PubsubOrderQueue(PublisherInterface publisher) {
        this.publisher = publisher;
    }

    @Override
    public CompletionStage<String> send(long cartId, Cart cart) {
        try {
            ApiFuture<String> futureId = publisher.publish(createMessage(cartId, cart));
            return ApiFuturesExtra.toCompletableFuture(futureId, ForkJoinPool.commonPool());
        } catch (Exception e) {
            LOGGER.error("Failed to send order [cartId={}, userId={}]", cartId, cart.getUserId());
            return CompletableFuture.failedFuture(e);
        }
    }

    private PubsubMessage createMessage(long cartId, Cart cart) throws IOException {
        return PubsubMessage.newBuilder()
                .setData(UnsafeByteOperations.unsafeWrap(AvroUtil.createOrder(cartId, cart).toByteBuffer()))
                .build();
    }
}
