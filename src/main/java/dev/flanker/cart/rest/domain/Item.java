package dev.flanker.cart.rest.domain;

import javax.validation.constraints.Min;
import java.util.Objects;

public class Item {
    @Min(1)
    private long itemId;

    @Min(0)
    private int number;

    public Item(long itemId, int number) {
        this.itemId = itemId;
        this.number = number;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return itemId == item.itemId &&
                number == item.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, number);
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", number=" + number +
                '}';
    }
}
