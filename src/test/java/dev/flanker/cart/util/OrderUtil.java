package dev.flanker.cart.util;

import dev.flanker.cart.domain.Item;
import dev.flanker.cart.generated.avro.OrderEntry;

public class OrderUtil {
    private OrderUtil() {}

    public static Item toItem(OrderEntry entry) {
        return new Item(entry.getItemId(), entry.getNumber());
    }
}
