package dev.flanker.cart.service;

import dev.flanker.cart.domain.Item;

import java.util.concurrent.CompletionStage;

public interface ItemService {
    /**
     * Put item by user id.
     *
     * @param userId Targeted user.
     * @param item Targeted item.
     * @return Returns a completion stage of void, that
     * completes exceptionally in case of fail.
     */
    CompletionStage<Void> put(long userId, Item item);

    /**
     * Get item from the cart by user id.
     *
     * @param userId Targeted user.
     * @param itemId Targeted item.
     * @return Returns completion stage of {@link Item}, that
     * in case of item absence in the cart contains null.
     */
    CompletionStage<Item> get(long userId, String itemId);

    /**
     * Increase number of item in the user cart.
     *
     * @param userId Targeted user.
     * @param item Item change.
     * @return Returns completion stage of item, that
     * contains an updated item.
     */
    CompletionStage<Item> increase(long userId, Item item);

    /**
     * Decrease number of item in the user cart.
     *
     * @param userId Targeted user.
     * @param item Item change.
     * @return Returns completion stage of item, that
     * contains an updated item.
     */
    CompletionStage<Item> decrease(long userId, Item item);

    /**
     * Delete item form a user cart.
     *
     * @param userId Targeted user.
     * @param itemId Targeted item.
     * @return Returns completion stage of boolean, that
     * indicates that operation was applied to item.
     */
    CompletionStage<Boolean> delete(long userId, String itemId);

}
