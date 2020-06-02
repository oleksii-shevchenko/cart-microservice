package dev.flanker.cart.service.impl;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.flanker.cart.db.BindingRepository;
import dev.flanker.cart.db.CartRepository;
import dev.flanker.cart.domain.Cart;
import dev.flanker.cart.service.CartService;

@Component
public class BaseCartService implements CartService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseCartService.class);

    private final BindingRepository bindingRepository;

    private final CartRepository cartRepository;

    @Autowired
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
                        LOGGER.info("Binding absent [userId={}]. Returns empty list.", userId);
                        return CompletableFuture.completedFuture(new ArrayList<>());
                    }
                })
                .thenApply(items -> new Cart(userId, items));
    }

    @Override
    public CompletionStage<Boolean> delete(long userId) {
        LOGGER.info("Preparing cart delete [userId={}].", userId);
        return bindingRepository.get(userId)
                .thenCompose(binding -> {
                    if (binding != null) {
                        return cartRepository.delete(binding.getCartId());
                    } else {
                        LOGGER.warn("Failed to delete items because a binding absent [userId={}]", userId);
                        return CompletableFuture.completedFuture(false);
                    }
                })
                .thenCompose(itemDelete -> {
                    if (itemDelete) {
                        return bindingRepository.delete(userId);
                    } else {
                        return CompletableFuture.completedFuture(false);
                    }
                });
    }
}
