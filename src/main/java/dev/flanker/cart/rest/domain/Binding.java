package dev.flanker.cart.rest.domain;

import javax.validation.constraints.Min;
import java.util.Objects;

public class Binding {
    @Min(1)
    private long userId;

    @Min(1)
    private long cartId;

    public Binding(@Min(1) long userId, @Min(1) long cartId) {
        this.userId = userId;
        this.cartId = cartId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Binding binding = (Binding) o;
        return userId == binding.userId &&
                cartId == binding.cartId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, cartId);
    }

    @Override
    public String toString() {
        return "Binding{" +
                "userId=" + userId +
                ", cartId=" + cartId +
                '}';
    }
}
