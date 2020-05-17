package dev.flanker.cart.queue.gcp;

import dev.flanker.cart.generated.avro.Order;
import dev.flanker.cart.generated.avro.OrderEntry;
import dev.flanker.cart.rest.domain.Cart;
import dev.flanker.cart.rest.domain.Item;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AvroUtilTest {
    @Test
    public void createEntryFailTest() {
        assertThrows(IllegalArgumentException.class, () -> AvroUtil.createEntry( new Item(12, null)));
    }

    @Test
    public void createEntryOkTest() {
        Item item = new Item(14, 2);
        OrderEntry entry = AvroUtil.createEntry(item);

        assertEquals(item.getItemId(), entry.getItemId());
        assertEquals(item.getNumber(), entry.getNumber());
    }

    @Test
    public void createOrderFailTest() {
        Item item = new Item(11, null);
        Cart cart = new Cart(14, List.of(item));

        assertThrows(IllegalArgumentException.class, () -> AvroUtil.createOrder(66, cart));
    }

    @Test
    public void createOrderOkTest() {
        Item item = new Item(11, 3);
        Cart cart = new Cart(14, List.of(item));

        Order order = AvroUtil.createOrder(66, cart);

        assertEquals(1, order.getEntries().size());

        OrderEntry entry = order.getEntries().get(0);

        assertEquals(66, order.getCartId());
        assertEquals(14, order.getUserId());
        assertEquals(11, entry.getItemId());
        assertEquals(3, entry.getNumber());
    }
}
