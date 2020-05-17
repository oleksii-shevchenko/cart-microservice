package dev.flanker.cart.service.gcp;

import dev.flanker.cart.db.BindingRepository;
import dev.flanker.cart.db.CartRepository;
import dev.flanker.cart.rest.domain.Binding;
import dev.flanker.cart.rest.domain.Cart;
import dev.flanker.cart.rest.domain.Item;
import dev.flanker.cart.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class GcpCartService implements CartService {
    @Override
    public Cart get(long userId) {
        return null;
    }

    @Override
    public Cart delete(long userId) {
        return null;
    }
    /*private static final Logger LOGGER = LoggerFactory.getLogger(GcpCartService.class);

    private final CartRepository cartRepository;

    private final BindingRepository bindingRepository;

    public GcpCartService(CartRepository cartRepository, BindingRepository bindingRepository) {
        this.cartRepository = cartRepository;
        this.bindingRepository = bindingRepository;
    }

    @Override
    public Cart get(long userId) {
        Optional<Binding> binding = bindingRepository.get(userId);
        if (binding.isPresent()) {
            List<Item> items = cartRepository.get(binding.get().getCartId());
            return new Cart(userId, items);
        } else {
            LOGGER.info("Missing binding for user [userId={}]. Return empty list", userId);
            return new Cart(userId, new ArrayList<>());
        }
    }

    @Override
    public Cart delete(long userId) {
        Optional<Binding> optionalBinding = bindingRepository.delete(userId);
        if (optionalBinding.isPresent()) {
            Binding binding = optionalBinding.get();
            LOGGER.info("Deleted binding [userId={}, cartId={}]", userId, binding.getCartId());

            List<Item> items = cartRepository.delete(binding.getCartId());
            LOGGER.info("Cleaned cart for user [userId={}, cartId={}]", userId, binding.getCartId());

            return new Cart(userId, items);
        } else {
            LOGGER.info("Missing binding for user [userId={}]. Return empty list", userId);
            return new Cart(userId, new ArrayList<>());
        }
    }*/
}
