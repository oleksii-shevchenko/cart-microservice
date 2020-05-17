package dev.flanker.cart.service.impl;

import dev.flanker.cart.db.BindingRepository;
import dev.flanker.cart.db.CartRepository;
import dev.flanker.cart.rest.domain.Cart;
import dev.flanker.cart.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class BaseCartService implements CartService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseCartService.class);

    private final BindingRepository bindingRepository;

    private final CartRepository cartRepository;

    public BaseCartService(BindingRepository bindingRepository, CartRepository cartRepository) {
        this.bindingRepository = bindingRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public CompletionStage<Cart> get(long userId) {
        return bindingRepository.get(userId)
                .thenCompose(binding -> {
                    if (binding != null) {
                        return cartRepository.get(binding.getCartId());
                    } else {
                        return CompletableFuture.completedFuture(new ArrayList<>());
                    }
                })
                .thenApply(items -> new Cart(userId, items));
    }

    @Override
    public CompletionStage<Void> delete(long userId) {
        return bindingRepository.get(userId)
                .thenCompose(binding -> {
                    if (binding != null) {
                        return cartRepository.delete(binding.getCartId());
                    } else {
                        return null;
                    }
                })
                .thenCompose(v -> bindingRepository.delete(userId));
    }
}
