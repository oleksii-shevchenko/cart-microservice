package dev.flanker.cart.queue.gcp;

import com.google.cloud.pubsub.v1.PublisherInterface;
import dev.flanker.cart.rest.domain.Cart;
import dev.flanker.cart.rest.domain.Item;
import dev.flanker.cart.util.AsyncUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.CompletionStage;

import static dev.flanker.cart.util.AsyncUtil.isCompletedExceptionally;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PubsubOrderQueueTest {
    @InjectMocks
    private PubsubOrderQueue queue;

    @Mock
    private PublisherInterface publisher;

    @AfterEach
    public void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(publisher);
    }

    @Test
    public void badRequestFailTest() {
        assertTrue(isCompletedExceptionally(queue.send(12, new Cart(11, List.of(new Item(15, null))))));
        assertTrue(isCompletedExceptionally(queue.send(12, new Cart(11, List.of(new Item(15, -1))))));
    }

    @Test
    public void sendRequestFailTest() {
        String id = "TEST_ID";
        when(publisher.publish(any())).thenReturn(AsyncUtil.completedFuture(id));
        CompletionStage<String> completedId = queue.send(12, new Cart(11, List.of(new Item(15, 1))));
        assertEquals(id, completedId.toCompletableFuture().join());
        verify(publisher).publish(any());
    }
}
