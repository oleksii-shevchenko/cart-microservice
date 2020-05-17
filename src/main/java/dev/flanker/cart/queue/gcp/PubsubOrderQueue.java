package dev.flanker.cart.queue.gcp;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.PublisherInterface;
import com.google.protobuf.UnsafeByteOperations;
import com.google.pubsub.v1.PubsubMessage;
import com.spotify.futures.ApiFuturesExtra;
import com.spotify.futures.FuturesExtra;
import dev.flanker.cart.queue.OrderQueue;
import dev.flanker.cart.rest.domain.Cart;
import io.grpc.netty.shaded.io.netty.util.concurrent.CompleteFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ForkJoinPool;

public class PubsubOrderQueue implements OrderQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(PubsubOrderQueue.class);

    private final PublisherInterface publisher;

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
