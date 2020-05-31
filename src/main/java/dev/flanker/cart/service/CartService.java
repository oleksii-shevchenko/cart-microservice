package dev.flanker.cart.service;

import dev.flanker.cart.domain.Cart;

import java.util.concurrent.CompletionStage;

public interface CartService {
    /**
     * Get cart content by a user id.
     *
     * @param userId Targeted user.
     * @return Returns completion stage of user cart.
     */
    CompletionStage<Cart> get(long userId);

    /**
     * Delete cart content and user binding by a user id.
     * @param userId Targeted user
     * @return Returns completion stage of boolean, that
     * indicates that operation was applied to at least
     * one item in the cart.
     */
    CompletionStage<Boolean> delete(long userId);
}
