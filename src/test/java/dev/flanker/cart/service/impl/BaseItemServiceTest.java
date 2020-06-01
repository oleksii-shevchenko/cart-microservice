package dev.flanker.cart.service.impl;

import dev.flanker.cart.db.BindingRepository;
import dev.flanker.cart.db.CartRepository;
import dev.flanker.cart.domain.Binding;
import dev.flanker.cart.domain.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaseItemServiceTest {
    @InjectMocks
    private BaseItemService itemService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BindingRepository bindingRepository;

    @Mock
    private Supplier<Long> idGenerator;

    @BeforeEach
    public void noMoreInteractions() {
        verifyNoMoreInteractions(cartRepository, bindingRepository, idGenerator);
    }

    @Test
    public void bindingAbsentPutTest() {
        long userId = 1234888321L;
        Binding binding = new Binding(userId, 456);
        Item item = new Item("1234",4);

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(null));
        when(idGenerator.get()).thenReturn(binding.getCartId());
        when(bindingRepository.put(binding)).thenReturn(CompletableFuture.completedFuture(null));
        when(cartRepository.put(binding.getCartId(), item)).thenReturn(CompletableFuture.completedFuture(null));

        itemService.put(userId, item).toCompletableFuture().join();

        verify(bindingRepository).get(userId);
        verify(bindingRepository).put(binding);
        verify(cartRepository).put(binding.getCartId(), item);
    }

    @Test
    public void putOkTest() {
        long userId = 1234888321L;
        Binding binding = new Binding(userId, 456);
        Item item = new Item("1234",4);

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(binding));
        when(cartRepository.put(binding.getCartId(), item)).thenReturn(CompletableFuture.completedFuture(null));

        itemService.put(userId, item).toCompletableFuture().join();

        verify(bindingRepository).get(userId);
        verify(cartRepository).put(binding.getCartId(), item);
    }

    @Test
    public void bindingAbsentOkTest() {
        long userId = 1234888321L;

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(null));

        Item item = itemService.get(userId, "1234").toCompletableFuture().join();

        assertNull(item);

        verify(bindingRepository).get(userId);
    }

    @Test
    public void getOkTest() {
        long userId = 1234888321L;
        Binding binding = new Binding(userId, 456);
        Item item = new Item("1234",4);

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(binding));
        when(cartRepository.get(binding.getCartId(), item.getItemId())).thenReturn(CompletableFuture.completedFuture(item));

        Item actual = itemService.get(userId, "1234").toCompletableFuture().join();

        assertEquals(item, actual);

        verify(bindingRepository).get(userId);
        verify(cartRepository).get(binding.getCartId(), item.getItemId());
    }

    @Test
    public void bindingAbsentIncreaseTest() {
        long userId = 1234888321L;

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(null));

        try {
            itemService.increase(userId, new Item("1234", 1)).toCompletableFuture().join();
            fail();
        } catch (Exception e) {
            // Expected
        }

        verify(bindingRepository).get(userId);
    }

    @Test
    public void updatedIncreaseTest() {
        long userId = 1234888321L;
        Binding binding = new Binding(userId, 456);
        String itemId = "123234";
        Item patch = new Item(itemId, 1);
        Item updated = new Item(itemId, 5);

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(binding));
        when(cartRepository.get(binding.getCartId(), itemId)).thenReturn(CompletableFuture.completedFuture(new Item(itemId, 4)));
        when(cartRepository.update(binding.getCartId(), updated)).thenReturn(CompletableFuture.completedFuture(true));

        Item actual = itemService.updateItemQuantity(userId, patch.getItemId(), patch.getNumber()).toCompletableFuture().join();

        assertEquals(updated, actual);

        verify(bindingRepository).get(userId);
        verify(cartRepository).get(binding.getCartId(), itemId);
        verify(cartRepository).update(binding.getCartId(), updated);
    }

    @Test
    public void notUpdatedIncreaseTest() {
        long userId = 1234888321L;
        Binding binding = new Binding(userId, 456);
        String itemId = "1234";
        Item patch = new Item(itemId, 1);
        Item updated = new Item(itemId, 5);

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(binding));
        when(cartRepository.get(binding.getCartId(), itemId)).thenReturn(CompletableFuture.completedFuture(new Item(itemId, 4)));
        when(cartRepository.update(binding.getCartId(), updated)).thenReturn(CompletableFuture.completedFuture(false));

        try {
            itemService.increase(userId, patch).toCompletableFuture().join();
            fail();
        } catch (Exception e) {
            // Expected
        }


        verify(bindingRepository).get(userId);
        verify(cartRepository).get(binding.getCartId(), itemId);
        verify(cartRepository).update(binding.getCartId(), updated);
    }

    @Test
    public void bindingAbsentDeleteFailTest() {
        long userId = 1234888321L;

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(null));

        try {
            itemService.delete(userId, "1234").toCompletableFuture().join();
            fail();
        } catch (Exception e) {
            // Expected
        }

        verify(bindingRepository).get(userId);
    }

    @Test
    public void deleteOkTest() {
        long userId = 1234888321L;
        String itemId = "12435";
        Binding binding = new Binding(userId, 956834);

        when(bindingRepository.get(userId)).thenReturn(CompletableFuture.completedFuture(binding));
        when(cartRepository.delete(binding.getCartId(), itemId)).thenReturn(CompletableFuture.completedFuture(true));

        Boolean deleted = itemService.delete(userId, itemId).toCompletableFuture().join();

        assertNotNull(deleted);
        assertTrue(deleted);

        verify(bindingRepository).get(userId);
        verify(cartRepository).delete(binding.getCartId(), itemId);
    }
}