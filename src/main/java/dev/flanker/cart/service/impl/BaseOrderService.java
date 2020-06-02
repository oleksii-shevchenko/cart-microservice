package dev.flanker.cart.service.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.flanker.cart.db.BindingRepository;
import dev.flanker.cart.db.CartRepository;
import dev.flanker.cart.domain.Binding;
import dev.flanker.cart.domain.Cart;
import dev.flanker.cart.domain.Item;
import dev.flanker.cart.queue.OrderQueue;
import dev.flanker.cart.service.OrderService;

@Component
public class BaseOrderService implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseOrderService.class);

    private final BindingRepository bindingRepository;

    private final CartRepository cartRepository;

    private final OrderQueue orderQueue;

    @Autowired
    public BaseOrderService(BindingRepository bindingRepository, CartRepository cartRepository, OrderQueue orderQueue) {
        this.bindingRepository = bindingRepository;
        this.cartRepository = cartRepository;
        this.orderQueue = orderQueue;
    }

    @Override
    public CompletionStage<Cart> completeOrder(long userId) {
        LOGGER.info("Preparing order sending [userId={}]", userId);
        CompletableFuture<Binding> bindingFuture = bindingRepository.get(userId).toCompletableFuture();

        CompletionStage<OrderDto> orderFuture = bindingFuture
                .thenCompose(binding -> {
                    if (binding != null) {
                        return cartRepository.get(binding.getCartId());
                    } else {
                        LOGGER.error("No binding. Failed to create order [userId={}]", userId);
                        throw new IllegalStateException("No active user-cart binding");
                    }
                })
                .thenCombine(bindingFuture, OrderDto::of);

        // Binding future must be completed by this time.
        // Cart dto must be not null in downstream.
        return orderFuture.thenCompose(dto -> orderQueue.send(dto.getCartId(), dto.getCart()))
                .thenCompose(id -> bindingRepository.delete(userId))
                .thenCompose(deleted-> cartRepository.delete(bindingFuture.join().getCartId()))
                .thenCompose(delete -> orderFuture)
                .thenApply(OrderDto::getCart);
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
            if (items.isEmpty()) {
                throw new IllegalStateException("Cart cannot be empty");
            }
            return new OrderDto(binding.getCartId(), new Cart(binding.getUserId(), items));
        }
    }
}
