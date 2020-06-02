package dev.flanker.cart.queue.gcp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import dev.flanker.cart.domain.Cart;
import dev.flanker.cart.domain.Item;
import dev.flanker.cart.generated.avro.Order;
import dev.flanker.cart.generated.avro.OrderEntry;

class AvroUtilTest {
    @Test
    public void createEntryFailTest() {
        assertThrows(IllegalArgumentException.class, () -> AvroUtil.createEntry( new Item("12", -2)));
    }

    @Test
    public void createEntryOkTest() {
        Item item = new Item("14", 2);
        OrderEntry entry = AvroUtil.createEntry(item);

        assertEquals(item.getItemId(), entry.getItemId());
        assertEquals(item.getNumber(), entry.getNumber());
    }

    @Test
    public void createOrderFailTest() {
        long userId = 5435345;
        Item item = new Item("11", -1);
        Cart cart = new Cart(userId, List.of(item));

        assertThrows(IllegalArgumentException.class, () -> AvroUtil.createOrder(66, cart));
    }

    @Test
    public void createOrderOkTest() {
        long uuid = 47567576;
        Item item = new Item("11", 3);
        Cart cart = new Cart(uuid, List.of(item));

        Order order = AvroUtil.createOrder(66, cart);

        assertEquals(1, order.getEntries().size());

        OrderEntry entry = order.getEntries().get(0);

        assertEquals(66, order.getCartId());
        assertEquals("11", entry.getItemId());
        assertEquals(3, entry.getNumber());
    }
}
