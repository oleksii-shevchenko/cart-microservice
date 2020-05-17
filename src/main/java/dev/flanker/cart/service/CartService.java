package dev.flanker.cart.service;

import dev.flanker.cart.rest.domain.Cart;
import dev.flanker.cart.rest.domain.Item;

import java.util.concurrent.CompletionStage;

public interface CartService {
    CompletionStage<Cart> get(long userId);

    CompletionStage<Void> delete(long userId);
}
