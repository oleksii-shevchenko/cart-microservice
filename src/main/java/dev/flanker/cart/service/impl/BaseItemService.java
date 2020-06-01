package dev.flanker.cart.service.impl;

import dev.flanker.cart.db.BindingRepository;
import dev.flanker.cart.db.CartRepository;
import dev.flanker.cart.domain.Binding;
import dev.flanker.cart.domain.Item;
import dev.flanker.cart.exception.NotFoundException;
import dev.flanker.cart.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@Component
public class BaseItemService implements ItemService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseItemService.class);

    private final BindingRepository bindingRepository;

    private final CartRepository cartRepository;

    private final Supplier<Long> idGenerator;

    @Autowired
    public BaseItemService(BindingRepository bindingRepository,
                           CartRepository cartRepository,
                           Supplier<Long> idGenerator) {
        this.bindingRepository = bindingRepository;
        this.cartRepository = cartRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public CompletionStage<Void> put(long userId, Item item) {
        return bindingRepository.get(userId)
                .thenCompose(binding -> {
                    if (binding != null) {
                        return CompletableFuture.completedFuture(binding);
                    } else {
                        Binding bind = new Binding(userId, idGenerator.get());
                        LOGGER.info("Binding absent. Generated new binding [{}]", bind);
                        return bindingRepository.put(bind).thenApply(v -> bind);
                    }
                })
                .thenCompose(binding -> {
                    if (binding != null) {
                        return cartRepository.put(binding.getCartId(), item);
                    } else {
                        LOGGER.error("Illegal state. Binding must not be null after generation.");
                        throw new IllegalStateException("Binding should not be null after insert");
                    }
                });
    }

    @Override
    public CompletionStage<Item> get(long userId, String itemId) {
        return bindingRepository.get(userId)
                .thenCompose(binding -> {
                    if (binding != null) {
                        return cartRepository.get(binding.getCartId(), itemId);
                    } else {
                        LOGGER.warn("Binding for user not found [userId={}]. Return null.", userId);
                        return CompletableFuture.completedFuture(null);
                    }
                });
    }

    @Override
    public CompletionStage<Item> increase(long userId, Item item) {
        return updateItemQuantity(userId, item.getItemId(), item.getNumber());
    }

    @Override
    public CompletionStage<Item> decrease(long userId, Item item) {
        return updateItemQuantity(userId, item.getItemId(), -item.getNumber());
    }

    @Override
    public CompletionStage<Boolean> delete(long userId, String itemId) {
        return bindingRepository.get(userId)
                .thenCompose(binding -> {
                    if (binding != null) {
                        return cartRepository.delete(binding.getCartId(), itemId);
                    } else {
                        LOGGER.warn("Cannot delete. Binding for user not found [userId={}].", userId);
                        throw new NotFoundException();
                    }
                });
    }

    CompletableFuture<Item> updateItemQuantity(long userId, String itemId, int diff) {
        CompletableFuture<Binding> bindingFuture = bindingRepository.get(userId).toCompletableFuture();

        CompletableFuture<Integer> differenceFuture = bindingFuture
                .thenCompose(binding -> {
                    if (binding != null) {
                        return cartRepository.get(binding.getCartId(), itemId);
                    } else {
                        throw new NotFoundException();
                    }
                })
                .thenApply(persisted -> {
                    if (persisted != null && persisted.getNumber() + diff > 0) {
                        return persisted.getNumber() + diff;
                    } else {
                        throw new NotFoundException();
                    }
                });

        return differenceFuture
                .thenCompose(number -> cartRepository.update(bindingFuture.join().getCartId(), new Item(itemId, number)))
                .thenApply(updated -> {
                    if (updated != null && updated) {
                        return new Item(itemId, differenceFuture.join());
                    } else {
                        throw new NotFoundException();
                    }
                });
    }
}
