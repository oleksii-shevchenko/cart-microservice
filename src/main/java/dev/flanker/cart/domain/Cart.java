package dev.flanker.cart.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class Cart {
    @Min(1)
    private long userId;

    @NotNull
    private List<Item> items;

    public Cart() {}

    public Cart(@Min(1) long userId, @NotNull List<Item> items) {
        this.userId = userId;
        this.items = items;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return userId == cart.userId &&
                items.equals(cart.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, items);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "userId=" + userId +
                ", items=" + items +
                '}';
    }
}
