package dev.flanker.cart.db;

import dev.flanker.cart.rest.domain.Item;

import java.util.List;
import java.util.Optional;

public interface CartRepository {
    void put(long cartId, Item item);

    List<Item> get(long cartId);

    Optional<Item> get(long cartId, long itemId);

    List<Item> delete(long cartId);

    Optional<Item> delete(long cartId, long itemId);
}
