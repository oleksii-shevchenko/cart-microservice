package dev.flanker.cart.rest;

import dev.flanker.cart.domain.Cart;
import dev.flanker.cart.service.CartService;
import dev.flanker.cart.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class CartFluxController {
    private final CartService cartService;

    private final OrderService orderService;

    @Autowired
    public CartFluxController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @ResponseBody
    @GetMapping("/api/v1/cart/{userId}")
    public Mono<Cart> getCart(@PathVariable long userId) {
        return Mono.fromCompletionStage(cartService.get(userId));
    }

    @ResponseBody
    @PostMapping("/api/v1/cart/{userId}")
    public Mono<Cart> commitCart(@PathVariable long userId) {
        return Mono.fromCompletionStage(orderService.completeOrder(userId));
    }

    @ResponseBody
    @DeleteMapping("/api/v1/cart/{userId}")
    public Mono<Boolean> deleteCart(@PathVariable long userId) {
        return Mono.fromCompletionStage(cartService.delete(userId));
    }
}
