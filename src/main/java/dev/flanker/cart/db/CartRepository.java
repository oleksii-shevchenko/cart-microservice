package dev.flanker.cart.db;

import dev.flanker.cart.domain.Item;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface CartRepository {
    /**
     * Put specified item into a cart.
     *
     * @param cartId Targeted cart.
     * @param item Targeted item.
     * @return Returns a completion stage of void, that
     * completes exceptionally in case of fail.
     */
    CompletionStage<Void> put(long cartId, Item item);

    /**
     * Get specified item from a cart.
     *
     * @param cartId Targeted cart.
     * @param itemId Targeted item.
     * @return Returns completion stage of {@link Item}, that
     * in case of item absence in the cart contains null.
     */
    CompletionStage<Item> get(long cartId, String itemId);

    /**
     * Get items from a cart.
     *
     * @param cartId Targeted cart.
     * @return Returns completion stage of items list, that
     * in case of empty cart contains empty list.
     */
    CompletionStage<List<Item>> get(long cartId);

    /**
     * Delete all items from a cart.
     *
     * @param cartId Targeted item.
     * @return Returns completion stage of boolean, that
     * indicates that operation was applied to at least one
     * item.
     */
    CompletionStage<Boolean> delete(long cartId);

    /**
     * Delete specified from a cart.
     *
     * @param cartId Targeted cart.
     * @param itemId Targeted item.
     * @return Returns completion stage of boolean, that
     * indicates that operation was applied to item.
     */
    CompletionStage<Boolean> delete(long cartId, String itemId);

    /**
     * Updated item in a cart.
     *
     * @param cartId Targeted cart.
     * @param item New item version.
     * @return Returns completion stage of boolean, that
     * contains indicates that item was updated.
     */
    CompletionStage<Boolean> update(long cartId, Item item);
}
