package dev.flanker.cart.service.gcp;

import dev.flanker.cart.db.BindingRepository;
import dev.flanker.cart.db.CartRepository;
import dev.flanker.cart.rest.domain.Binding;
import dev.flanker.cart.rest.domain.Cart;
import dev.flanker.cart.rest.domain.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GcpCartServiceTest {
    private static final long USER_ID = 7;

    private static final long CART_ID = 11;

    private static final Binding BINDING = new Binding(USER_ID, CART_ID);

    @InjectMocks
    private GcpCartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BindingRepository bindingRepository;

    @Mock
    private Supplier<Long> cartIdGenerator;

    @BeforeEach
    public void verifyNoInteractions() {
        verifyNoMoreInteractions(cartRepository, bindingRepository, cartIdGenerator);
    }

    @Test
    public void ifBindingAbsentGetTest() {
        when(bindingRepository.get(USER_ID)).thenReturn(Optional.empty());

        Cart cart = cartService.get(USER_ID);

        assertEquals(USER_ID, cart.getUserId());
        assertTrue(cart.getItems().isEmpty());

        verify(bindingRepository).get(USER_ID);
    }

    @Test
    public void ifBindingPresentGetTest() {
        List<Item> items = List.of(new Item(12, 12));

        when(bindingRepository.get(USER_ID)).thenReturn(Optional.of(BINDING));
        when(cartRepository.get(CART_ID)).thenReturn(items);

        assertEquals(new Cart(USER_ID, items), cartService.get(USER_ID));

        verify(bindingRepository).get(USER_ID);
        verify(cartRepository).get(CART_ID);
    }

    @Test
    public void ifBindingAbsentDeleteTest() {
        when(bindingRepository.delete(USER_ID)).thenReturn(Optional.empty());

        Cart cart = cartService.delete(USER_ID);

        assertEquals(USER_ID, cart.getUserId());
        assertTrue(cart.getItems().isEmpty());

        verify(bindingRepository).delete(USER_ID);
    }

    @Test
    public void ifBindingPresentDeleteTest() {
        List<Item> items = List.of(new Item(12, 12));

        when(bindingRepository.delete(USER_ID)).thenReturn(Optional.of(BINDING));
        when(cartRepository.delete(CART_ID)).thenReturn(items);

        assertEquals(new Cart(USER_ID, items), cartService.delete(USER_ID));

        verify(bindingRepository).delete(USER_ID);
        verify(cartRepository).delete(CART_ID);
    }
}