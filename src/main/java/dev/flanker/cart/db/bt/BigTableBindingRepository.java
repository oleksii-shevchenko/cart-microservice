package dev.flanker.cart.db.bt;

import dev.flanker.cart.db.BindingRepository;
import dev.flanker.cart.rest.domain.Binding;

import java.util.Optional;

public class BigTableBindingRepository implements BindingRepository {
    @Override
    public void put(Binding binding) {

    }

    @Override
    public Optional<Binding> get(long userId) {
        return null;
    }

    @Override
    public Optional<Binding> delete(long userId) {
        return null;
    }
}
