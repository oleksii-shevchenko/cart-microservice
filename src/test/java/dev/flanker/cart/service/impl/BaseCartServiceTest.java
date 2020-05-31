package dev.flanker.cart.service.impl;

import dev.flanker.cart.db.BindingRepository;
import dev.flanker.cart.db.CartRepository;
import dev.flanker.cart.domain.Binding;
import dev.flanker.cart.domain.Cart;
import dev.flanker.cart.domain.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaseCartServiceTest {
    @InjectMocks
    private BaseCartService cartService;

    @Mock
    private BindingRepository bindingRepository;

    @Mock
    private CartRepository cartRepository;

    @BeforeEach
    public void noMoreInteractions() {
        verifyNoMoreInteractions(bindingRepository, cartRepository);
    }

    @Test
    public void noBindingGetOkTest() {
        long userId = 1234669L;

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(null));

        Cart cart = cartService.get(userId).toCompletableFuture().join();

        assertNotNull(cart);
        assertEquals(userId, cart.getUserId());
        assertTrue(cart.getItems().isEmpty());

        verify(bindingRepository).get(userId);
    }

    @Test
    public void bindingFailGetFailTest() {
        when(bindingRepository.get(anyLong())).thenReturn(CompletableFuture.failedFuture(new RuntimeException()));

        try {
            cartService.get(1234669L).toCompletableFuture().join();
            fail();
        } catch (Exception e) {
            // Expected
        }

        verify(bindingRepository).get(anyLong());
    }

    @Test
    public void itemsFailGetFailTest() {
        Binding binding = new Binding(1234669L, 455);
        when(bindingRepository.get(binding.getUserId())).thenReturn(CompletableFuture.completedFuture(binding));
        when(cartRepository.get(binding.getCartId())).thenReturn(CompletableFuture.failedFuture(new RuntimeException()));

        try {
            cartService.get(binding.getUserId()).toCompletableFuture().join();
            fail();
        } catch (Exception e) {
            // Expected
        }

        verify(bindingRepository).get(binding.getUserId());
        verify(cartRepository).get(binding.getCartId());
    }

    @Test
    public void getOkTest() {
        long userId = 1234669L;
        Binding binding = new Binding(userId, 1234);
        List<Item> items = List.of(new Item("11", 2), new Item("55", 1));

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(binding));
        when(cartRepository.get(binding.getCartId())).thenReturn(CompletableFuture.completedFuture(items));

        Cart cart = cartService.get(userId).toCompletableFuture().join();

        assertNotNull(cart);
        assertEquals(userId, cart.getUserId());
        assertEquals(items, cart.getItems());

        verify(bindingRepository).get(userId);
        verify(cartRepository).get(binding.getCartId());
    }

    @Test
    public void noBindingDeleteOkTest() {
        long userId = 1234669L;

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(null));

        Boolean delete = cartService.delete(userId).toCompletableFuture().join();

        assertNotNull(delete);
        assertFalse(delete);

        verify(bindingRepository).get(userId);
    }

    @Test
    public void bindingFailDeleteFailTest() {
        when(bindingRepository.get(anyLong())).thenReturn(CompletableFuture.failedFuture(new RuntimeException()));

        try {
            cartService.delete(1234669L).toCompletableFuture().join();
            fail();
        } catch (Exception e) {
            // Expected
        }

        verify(bindingRepository).get(anyLong());
    }

    @Test
    public void deleteOkTest() {
        long userId = 1234669L;
        Binding binding = new Binding(userId, 1234);

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(binding));
        when(cartRepository.delete(binding.getCartId())).thenReturn(CompletableFuture.completedFuture(true));
        when(bindingRepository.delete(userId)).thenReturn(CompletableFuture.completedFuture(true));

        Boolean deleted = cartService.delete(userId).toCompletableFuture().join();

        assertNotNull(deleted);
        assertTrue(deleted);

        verify(bindingRepository).get(userId);
        verify(cartRepository).delete(binding.getCartId());
        verify(bindingRepository).delete(userId);
    }
}