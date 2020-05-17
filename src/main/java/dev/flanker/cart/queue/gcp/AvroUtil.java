package dev.flanker.cart.queue.gcp;

import dev.flanker.cart.generated.avro.Order;
import dev.flanker.cart.generated.avro.OrderEntry;
import dev.flanker.cart.rest.domain.Cart;
import dev.flanker.cart.rest.domain.Item;

import java.util.List;
import java.util.stream.Collectors;

final class AvroUtil {
    private AvroUtil() {}

    static Order createOrder(long cartId, Cart cart) {
        List<OrderEntry> entries = cart.getItems().stream()
                .map(AvroUtil::createEntry)
                .collect(Collectors.toList());
        return Order.newBuilder()
                .setCartId(cartId)
                .setUserId(cart.getUserId())
                .setEntries(entries)
                .build();
    }

    static OrderEntry createEntry(Item item) {
        if (!item.isNumberPresent() || item.getNumber() <= 0) {
            throw new IllegalArgumentException("Number of items must not be null and positive");
        }
        return OrderEntry.newBuilder()
                .setItemId(item.getItemId())
                .setNumber(item.getNumber())
                .build();
    }
}
