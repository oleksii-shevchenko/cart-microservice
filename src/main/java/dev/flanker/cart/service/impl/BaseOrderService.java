package dev.flanker.cart.service.impl;

import dev.flanker.cart.db.BindingRepository;
import dev.flanker.cart.db.CartRepository;
import dev.flanker.cart.queue.OrderQueue;
import dev.flanker.cart.rest.domain.Binding;
import dev.flanker.cart.rest.domain.Cart;
import dev.flanker.cart.rest.domain.Item;
import dev.flanker.cart.service.OrderService;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class BaseOrderService implements OrderService {
    private final BindingRepository bindingRepository;

    private final CartRepository cartRepository;

    private final OrderQueue orderQueue;

    public BaseOrderService(BindingRepository bindingRepository, CartRepository cartRepository, OrderQueue orderQueue) {
        this.bindingRepository = bindingRepository;
        this.cartRepository = cartRepository;
        this.orderQueue = orderQueue;
    }

    @Override
    public CompletionStage<Cart> completeOrder(long userId) {
        CompletionStage<Binding> bindingFuture = bindingRepository.get(userId);
        CompletionStage<OrderDto> orderFuture = bindingFuture
                .thenCompose(binding -> {
                    if (binding != null) {
                        return cartRepository.get(binding.getCartId());
                    } else {
                        return null;
                    }
                })
                .thenCombine(bindingFuture, OrderDto::of);
        return orderFuture.thenCompose(dto -> orderQueue.send(dto.getCartId(), dto.cart))
                .thenCombine(bindingFuture, (id, binding) -> bindingRepository.delete(binding.getUserId()))
                .thenCombine(bindingFuture, (v, binding) -> cartRepository.delete(binding.getCartId()))
                .thenCombine(orderFuture, (v, order) -> order.getCart());
    }

    private static final class OrderDto {
        private final long cartId;
        private final Cart cart;

        OrderDto(long cartId, Cart cart) {
            this.cartId = cartId;
            this.cart = cart;
        }

        public long getCartId() {
            return cartId;
        }

        public Cart getCart() {
            return cart;
        }

        static OrderDto of(List<Item> items, Binding binding) {
            return new OrderDto(binding.getCartId(), new Cart(binding.getUserId(), items));
        }
    }
}
