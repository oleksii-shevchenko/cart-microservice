package dev.flanker.cart.db;

import dev.flanker.cart.rest.domain.Binding;

import java.util.Optional;

public interface BindingRepository {
    void put(Binding binding);

    Optional<Binding> get(long userId);

    Optional<Binding> delete(long userId);
}
