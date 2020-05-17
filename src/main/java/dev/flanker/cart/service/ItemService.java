package dev.flanker.cart.service;

import dev.flanker.cart.rest.domain.Item;

import java.util.concurrent.CompletionStage;

public interface ItemService {
    CompletionStage<Item> increase(long userId, Item item);

    CompletionStage<Item> decrease(long userId, Item item);

    CompletionStage<Item> get(long userId, long itemId);
}
