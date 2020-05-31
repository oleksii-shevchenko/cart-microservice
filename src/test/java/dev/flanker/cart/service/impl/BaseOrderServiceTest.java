package dev.flanker.cart.service.impl;

import dev.flanker.cart.db.BindingRepository;
import dev.flanker.cart.db.CartRepository;
import dev.flanker.cart.domain.Binding;
import dev.flanker.cart.domain.Cart;
import dev.flanker.cart.domain.Item;
import dev.flanker.cart.queue.OrderQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaseOrderServiceTest {
    @InjectMocks
    private BaseOrderService orderService;

    @Mock
    private OrderQueue orderQueue;

    @Mock
    private BindingRepository bindingRepository;

    @Mock
    private CartRepository cartRepository;

    @BeforeEach
    public void noMoreInteractions() {
        verifyNoMoreInteractions(orderQueue, bindingRepository, cartRepository);
    }

    @Test
    public void failBindingTest() {
        when(bindingRepository.get(anyLong())).thenReturn(CompletableFuture.failedFuture(new RuntimeException()));
        try {
            orderService.completeOrder(772212321L).toCompletableFuture().join();
            fail();
        } catch (Exception e) {
            // Expected
        }
        verify(bindingRepository).get(anyLong());
    }

    @Test
    public void noBindingFailTest() {
        long userId = 772212321L;

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(null));
        try {
            orderService.completeOrder(772212321L).toCompletableFuture().join();
            fail();
        } catch (Exception e) {
            // Expected
        }
        verify(bindingRepository).get(anyLong());
    }

    @Test
    public void emptyCartFailTest() {
        long userId = 772212321L;
        Binding binding = new Binding(userId, 9767);

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(binding));
        when(cartRepository.get(binding.getCartId())).thenReturn(CompletableFuture.completedFuture(new ArrayList<>()));
        try {
            orderService.completeOrder(userId).toCompletableFuture().join();
            fail();
        } catch (Exception e) {
            // Expected
        }
        verify(bindingRepository).get(userId);
        verify(cartRepository).get(binding.getCartId());
    }

    @Test
    public void sentQueueFailTest() {
        long userId = 772212321L;
        Binding binding = new Binding(userId, 1234);
        List<Item> items = List.of(new Item("11", 2), new Item("55", 1));
        Cart cart = new Cart(userId, items);

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(binding));
        when(cartRepository.get(binding.getCartId())).thenReturn(CompletableFuture.completedFuture(items));
        when(orderQueue.send(binding.getCartId(), cart)).thenReturn(CompletableFuture.failedFuture(new RuntimeException()));
        try {
            orderService.completeOrder(userId).toCompletableFuture().join();
            fail();
        } catch (Exception e) {
            // Expected
        }
        verify(bindingRepository).get(userId);
        verify(cartRepository).get(binding.getCartId());
        verify(orderQueue).send(binding.getCartId(), cart);
    }

    @Test
    public void deleteBindingFailTest() {
        long userId = 772212321L;
        Binding binding = new Binding(userId, 1234);
        List<Item> items = List.of(new Item("11", 2), new Item("55", 1));
        Cart cart = new Cart(userId, items);
        String messageId = "id";

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(binding));
        when(cartRepository.get(binding.getCartId())).thenReturn(CompletableFuture.completedFuture(items));
        when(orderQueue.send(binding.getCartId(), cart)).thenReturn(CompletableFuture.completedFuture(messageId));
        when(bindingRepository.delete(userId)).thenReturn(CompletableFuture.failedFuture(new RuntimeException()));
        try {
            orderService.completeOrder(userId).toCompletableFuture().join();
            fail();
        } catch (Exception e) {
            // Expected
        }
        verify(bindingRepository).get(userId);
        verify(cartRepository).get(binding.getCartId());
        verify(orderQueue).send(binding.getCartId(), cart);
        verify(bindingRepository).delete(userId);
    }

    @Test
    public void deleteCartFailTest() {
        long userId = 772212321L;
        Binding binding = new Binding(userId, 1234);
        List<Item> items = List.of(new Item("11", 2), new Item("55", 1));
        Cart cart = new Cart(userId, items);
        String messageId = "id";

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(binding));
        when(cartRepository.get(binding.getCartId())).thenReturn(CompletableFuture.completedFuture(items));
        when(orderQueue.send(binding.getCartId(), cart)).thenReturn(CompletableFuture.completedFuture(messageId));
        when(bindingRepository.delete(userId)).thenReturn(CompletableFuture.completedFuture(true));
        when(cartRepository.delete(binding.getCartId())).thenReturn(CompletableFuture.failedFuture(new RuntimeException()));
        try {
            orderService.completeOrder(userId).toCompletableFuture().join();
            fail();
        } catch (Exception e) {
            // Expected
        }
        verify(bindingRepository).get(userId);
        verify(cartRepository).get(binding.getCartId());
        verify(orderQueue).send(binding.getCartId(), cart);
        verify(bindingRepository).delete(userId);
        verify(cartRepository).delete(binding.getCartId());
    }

    @Test
    public void okTest() {
        long userId = 772212321L;
        Binding binding = new Binding(userId, 1234);
        List<Item> items = List.of(new Item("11", 2), new Item("55", 1));
        Cart cart = new Cart(userId, items);
        String messageId = "id";

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(binding));
        when(cartRepository.get(binding.getCartId())).thenReturn(CompletableFuture.completedFuture(items));
        when(orderQueue.send(binding.getCartId(), cart)).thenReturn(CompletableFuture.completedFuture(messageId));
        when(bindingRepository.delete(userId)).thenReturn(CompletableFuture.completedFuture(true));
        when(cartRepository.delete(binding.getCartId())).thenReturn(CompletableFuture.completedFuture(true));

        Cart order = orderService.completeOrder(userId).toCompletableFuture().join();

        assertNotNull(order);
        assertEquals(cart, order);

        verify(bindingRepository).get(userId);
        verify(cartRepository).get(binding.getCartId());
        verify(orderQueue).send(binding.getCartId(), order);
        verify(bindingRepository).delete(userId);
        verify(cartRepository).delete(binding.getCartId());
    }
}