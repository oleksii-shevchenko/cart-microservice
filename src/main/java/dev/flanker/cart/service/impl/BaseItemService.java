package dev.flanker.cart.service.impl;

import dev.flanker.cart.db.BindingRepository;
import dev.flanker.cart.db.CartRepository;
import dev.flanker.cart.rest.domain.Item;
import dev.flanker.cart.service.ItemService;

import java.util.concurrent.CompletionStage;

public class BaseItemService implements ItemService {
    private final BindingRepository bindingRepository;

    private final CartRepository cartRepository;

    public BaseItemService(BindingRepository bindingRepository, CartRepository cartRepository) {
        this.bindingRepository = bindingRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public CompletionStage<Item> increase(long userId, Item item) {
        return bindingRepository.get(userId)
                .thenCompose(binding -> {
                    if (binding != null) {
                        return cartRepository.update(binding.getCartId(), item.getItemId(), item.getNumber());
                    } else {
                        return null;
                    }
                });
    }

    @Override
    public CompletionStage<Item> decrease(long userId, Item item) {
        return bindingRepository.get(userId)
                .thenCompose(binding -> {
                    if (binding != null) {
                        return cartRepository.update(binding.getCartId(), item.getItemId(), -item.getNumber());
                    } else {
                        return null;
                    }
                });
    }

    @Override
    public CompletionStage<Item> get(long userId, long itemId) {
        return bindingRepository.get(userId)
                .thenCompose(binding -> {
                    if (binding != null) {
                        return cartRepository.get(binding.getCartId(), itemId);
                    } else {
                        return null;
                    }
                });
    }
}
