package dev.flanker.cart.rest;

import dev.flanker.cart.rest.domain.Cart;
import dev.flanker.cart.rest.domain.Item;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {
    @GetMapping("/api/v1/cart/{userId}")
    public Cart getCart(@PathVariable long userId) {
        return null;
    }

    @ResponseBody
    @PostMapping("/api/v1/cart/{userId}")
    public Cart commitCart(@PathVariable long userId) {
        return null;
    }

    @ResponseBody
    @DeleteMapping("/api/v1/cart/{userId}")
    public Cart deleteCart(@PathVariable long userId) {
        return null;
    }

    @ResponseBody
    @GetMapping("/api/v1/cart/{userId}/{itemId}")
    public Item getItem(@PathVariable long userId, @PathVariable long itemId) {
        return null;
    }

    @ResponseBody
    @PostMapping("/api/v1/cart/{userId}/item")
    public Item addItem(@PathVariable long userId, @RequestBody Item item) {
        return null;
    }

    @ResponseBody
    @DeleteMapping("/api/v1/cart/{userId}/item")
    public Item deleteItem(@PathVariable long userId, @RequestBody Item item) {
        return null;
    }
}
