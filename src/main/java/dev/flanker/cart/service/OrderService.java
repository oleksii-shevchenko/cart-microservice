package dev.flanker.cart.service;

import java.util.concurrent.CompletionStage;

import dev.flanker.cart.domain.Cart;

public interface OrderService {
    /**
     * Prepared and sent order by user id. After
     * successful sending clean the cart and binding.
     *
     * @param userId Targeted user.
     * @return Returns completion stage of cart that
     * represent a user order.
     */
    CompletionStage<Cart> completeOrder(long userId);
}
