package dev.flanker.cart.service;

import dev.flanker.cart.rest.domain.Cart;
import dev.flanker.cart.rest.domain.Item;

public interface CartService {
    Cart get(long userId);

    Cart delete(long userId);
}
