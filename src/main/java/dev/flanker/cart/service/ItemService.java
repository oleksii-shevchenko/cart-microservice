package dev.flanker.cart.service;

import dev.flanker.cart.rest.domain.Item;

public interface ItemService {
    void increase(long userId, Item item);

    void decrease(long userId, Item item);

    Item get(long userId);
}
