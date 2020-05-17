package dev.flanker.cart.queue.pb;

import dev.flanker.cart.queue.OrderQueue;
import dev.flanker.cart.rest.domain.Cart;

import java.util.concurrent.CompletionStage;

public class PubSubOrderQueue implements OrderQueue {
    @Override
    public CompletionStage<Void> sent(long cartId, Cart cart) {
        return null;
    }
}
