package dev.flanker.cart.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import dev.flanker.cart.domain.Binding;
import dev.flanker.cart.domain.Cart;
import dev.flanker.cart.domain.Item;

public class RandomUtil {
    private RandomUtil() {}

    public static long userId() {
        return ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
    }

    public static long cartId() {
        return ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
    }

    public static String itemId() {
        return new BigInteger(12 * Byte.SIZE, ThreadLocalRandom.current()).toString(16);
    }

    public static int number() {
        return ThreadLocalRandom.current().nextInt(1, 10);
    }

    public static Item item() {
        return new Item(itemId(), number());
    }

    public static Binding binding() {
        return new Binding(userId(), cartId());
    }

    public static Cart cart() {
        int quantity = ThreadLocalRandom.current().nextInt(1, 5);
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            items.add(item());
        }
        return new Cart(userId(), items);
    }
}
