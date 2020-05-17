package dev.flanker.cart.db;

import dev.flanker.cart.rest.domain.Binding;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface BindingRepository {
    CompletionStage<Void> put(Binding binding);

    CompletionStage<Binding> get(long userId);

    CompletionStage<Void> delete(long userId);
}
