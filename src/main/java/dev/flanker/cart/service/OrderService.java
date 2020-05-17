package dev.flanker.cart.service;

import dev.flanker.cart.rest.domain.Cart;

public interface OrderService {
    Cart completeOrder(long userId);
}
