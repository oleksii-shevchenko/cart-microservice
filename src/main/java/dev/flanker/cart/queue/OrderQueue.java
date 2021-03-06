package dev.flanker.cart.queue;

import java.util.concurrent.CompletionStage;

import dev.flanker.cart.domain.Cart;

public interface OrderQueue {
    /**
     * This method prepares and sends an order to
     * orders queue.
     *
     * @param cartId Targeted cart
     * @param cart Targeted content
     * @return Return completion stage of string, that
     * contains generated by a queue for the order message.
     */
    CompletionStage<String> send(long cartId, Cart cart);
}
