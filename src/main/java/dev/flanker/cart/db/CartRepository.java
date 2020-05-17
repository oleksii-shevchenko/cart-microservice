package dev.flanker.cart.db;

import dev.flanker.cart.rest.domain.Item;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface CartRepository {
    CompletionStage<Void> put(long cartId, Item item);

    CompletionStage<Item> get(long cartId, long itemId);

    CompletionStage<List<Item>> get(long cartId);

    CompletionStage<Void> delete(long cartId);

    CompletionStage<Void> delete(long cartId, long itemId);

    CompletionStage<Item> update(long cartId, long itemId, int numberDifference);
}
