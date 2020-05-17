package dev.flanker.cart.queue;

import dev.flanker.cart.rest.domain.Cart;

import java.util.concurrent.CompletionStage;

public interface OrderQueue {
    CompletionStage<Void> sent(long cartId, Cart cart);
}
