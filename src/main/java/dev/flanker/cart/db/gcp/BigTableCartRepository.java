package dev.flanker.cart.db.gcp;

import dev.flanker.cart.db.CartRepository;
import dev.flanker.cart.rest.domain.Item;

import java.util.List;
import java.util.Optional;

public class BigTableCartRepository implements CartRepository {
    @Override
    public void put(long cartId, Item item) {

    }

    @Override
    public List<Item> get(long cartId) {
        return null;
    }

    @Override
    public Optional<Item> get(long cartId, long itemId) {
        return null;
    }

    @Override
    public List<Item> delete(long cartId) {
        return null;
    }

    @Override
    public Optional<Item> delete(long cartId, long itemId) {
        return null;
    }
}
