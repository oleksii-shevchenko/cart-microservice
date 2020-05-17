package dev.flanker.cart.service.gcp;

import dev.flanker.cart.rest.domain.Item;
import dev.flanker.cart.service.ItemService;

public class GcpItemService implements ItemService {
    @Override
    public void increase(long userId, Item item) {

    }

    @Override
    public void decrease(long userId, Item item) {

    }

    @Override
    public Item get(long userId) {
        return null;
    }

    /*
    @Override
    public void put(long userId, Item item) {
        Binding binding = bindingRepository.get(userId)
                .orElseGet(() -> {
                    Binding b = new Binding(userId, cartIdGenerator.get());
                    bindingRepository.put(b);
                    LOGGER.info("Missing binding for user [userId={}], generated id [cardId={}]", userId, b.getCartId());
                    return b;
                });
        cartRepository.put(binding.getCartId(), item);
        LOGGER.info("Inserted item [itemId={}] into cart [cartId={}]", binding.getCartId(), item.getItemId());
    }
     */
}
