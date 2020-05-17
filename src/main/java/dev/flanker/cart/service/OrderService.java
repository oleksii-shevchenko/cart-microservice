package dev.flanker.cart.service;

import dev.flanker.cart.rest.domain.Cart;

import java.util.concurrent.CompletionStage;

public interface OrderService {
    CompletionStage<Cart> completeOrder(long userId);
}
