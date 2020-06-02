package dev.flanker.cart.rest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import dev.flanker.cart.domain.Cart;
import dev.flanker.cart.domain.Item;
import dev.flanker.cart.service.CartService;
import dev.flanker.cart.service.OrderService;

@WebFluxTest(CartFluxController.class)
class CartFluxControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CartService cartService;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    public void noMoreInteractions() {
        Mockito.verifyNoMoreInteractions(cartService, orderService);
    }

    @Test
    public void getFailTest() {
        long uuid = 78678;

        when(cartService.get(uuid)).thenReturn(CompletableFuture.failedFuture(new RuntimeException()));

        webTestClient.get()
                .uri("/api/v1/cart/{userId}", uuid)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();

        verify(cartService).get(uuid);
    }

    @Test
    public void getCartFailTest() {
        long uuid = 409853048;
        Item item = new Item("12345", 3);
        Cart cart = new Cart(uuid, List.of(item));

        when(cartService.get(uuid)).thenReturn(CompletableFuture.completedFuture(cart));

        webTestClient.get()
                .uri("/api/v1/cart/{userId}", uuid)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cart.class).isEqualTo(cart);

        verify(cartService).get(uuid);
    }

    @Test
    public void deleteCartOkTest() {
        long uuid = 456456;

        when(cartService.delete(uuid)).thenReturn(CompletableFuture.completedFuture(true));

        webTestClient.delete()
                .uri("/api/v1/cart/{userId}", uuid)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class).isEqualTo(true);

        verify(cartService).delete(uuid);
    }

    @Test
    public void deleteCartFailTest() {
        long uuid = 390340958;

        when(cartService.delete(uuid)).thenReturn(CompletableFuture.failedFuture(new RuntimeException()));

        webTestClient.delete()
                .uri("/api/v1/cart/{userId}", uuid)
                .exchange()
                .expectStatus().is5xxServerError();

        verify(cartService).delete(uuid);
    }

    @Test
    public void commitOkTest() {
        long uuid = 454353;
        Item item = new Item("12345", 3);
        Cart cart = new Cart(uuid, List.of(item));

        when(orderService.completeOrder(uuid)).thenReturn(CompletableFuture.completedFuture(cart));

        webTestClient.post()
                .uri("/api/v1/cart/{userId}", uuid)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cart.class).isEqualTo(cart);

        verify(orderService).completeOrder(uuid);
    }
}
