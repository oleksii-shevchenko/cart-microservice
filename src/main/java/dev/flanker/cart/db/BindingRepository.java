package dev.flanker.cart.db;

import java.util.concurrent.CompletionStage;

import dev.flanker.cart.domain.Binding;

public interface BindingRepository {
    /**
     * Put user-cart binding.
     *
     * @param binding Binding.
     * @return Returns a completion stage of void, that
     * completes exceptionally in case of fail.
     */
    CompletionStage<Void> put(Binding binding);

    /**
     * Get user-item binding.
     *
     * @param userId Targeted user.
     * @return Returns a completion stage of binding, that
     * contains null in case of binding absence.
     */
    CompletionStage<Binding> get(long userId);

    /**
     * Delete user-item binding.
     *
     * @param userId Targeted user.
     * @return Returns completion stage of boolean, that
     * indicates that operation was applied to at least one binding.
     */
    CompletionStage<Boolean> delete(long userId);
}
