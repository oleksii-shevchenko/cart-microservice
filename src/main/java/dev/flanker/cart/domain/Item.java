package dev.flanker.cart.domain;

import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Item {
    @NotNull
    @Pattern(regexp = "^[0-9a-f]{1,24}$")
    private String itemId;

    @Min(1)
    private int number;

    public Item() {}

    public Item(@NotNull @Pattern(regexp = "^[0-9a-f]{1,24}$") String itemId, @Min(1) int number) {
        this.itemId = itemId;
        this.number = number;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return number == item.number &&
                itemId.equals(item.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, number);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", number=" + number +
                '}';
    }
}
