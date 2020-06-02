package dev.flanker.cart.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import dev.flanker.cart.CartMicroserviceApplication;
import dev.flanker.cart.component.SimpleConsumer;
import dev.flanker.cart.db.BindingRepository;
import dev.flanker.cart.domain.Binding;
import dev.flanker.cart.domain.Cart;
import dev.flanker.cart.domain.Item;
import dev.flanker.cart.generated.avro.Order;
import dev.flanker.cart.generated.avro.OrderEntry;
import dev.flanker.cart.util.OrderUtil;
import dev.flanker.cart.util.RandomUtil;

@SpringBootTest(classes = CartMicroserviceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CartFluxControllerIntTest {
    private final WebTestClient webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:8080")
            .build();

    @Autowired
    private SimpleConsumer simpleConsumer;

    @Autowired
    private BindingRepository bindingRepository;

    @Test
    public void cleanTest() {
        Cart cart = RandomUtil.cart();

        webTestClient.get()
                .uri("/api/v1/cart/{userId}", cart.getUserId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cart.class).isEqualTo(new Cart(cart.getUserId(), new ArrayList<>()));

        for (Item item : cart.getItems()) {
            webTestClient.put()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/cart/{userId}/{itemId}")
                            .queryParam("number", item.getNumber())
                            .build(cart.getUserId(), item.getItemId()))
                    .exchange()
                    .expectStatus().isOk();
        }

        Cart responseBody = webTestClient.get()
                .uri("/api/v1/cart/{userId}", cart.getUserId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cart.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(responseBody);
        assertEquals(cart.getUserId(), responseBody.getUserId());
        assertEquals(new HashSet<>(cart.getItems()), new HashSet<>(responseBody.getItems()));

        webTestClient.delete()
                .uri("/api/v1/cart/{userId}", cart.getUserId())
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri("/api/v1/cart/{userId}", cart.getUserId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cart.class).isEqualTo(new Cart(cart.getUserId(), new ArrayList<>()));
    }

    @Test
    public void commitTest() throws Exception {
        Cart cart = RandomUtil.cart();

        webTestClient.get()
                .uri("/api/v1/cart/{userId}", cart.getUserId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cart.class).isEqualTo(new Cart(cart.getUserId(), new ArrayList<>()));

        for (Item item : cart.getItems()) {
            webTestClient.put()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/cart/{userId}/{itemId}")
                            .queryParam("number", item.getNumber())
                            .build(cart.getUserId(), item.getItemId()))
                    .exchange()
                    .expectStatus().isOk();
        }

        Cart responseBody = webTestClient.get()
                .uri("/api/v1/cart/{userId}", cart.getUserId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cart.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(responseBody);
        assertEquals(cart.getUserId(), responseBody.getUserId());
        assertEquals(new HashSet<>(cart.getItems()), new HashSet<>(responseBody.getItems()));

        Binding binding = bindingRepository.get(cart.getUserId()).toCompletableFuture().join();

        Cart post = webTestClient.post()
                .uri("/api/v1/cart/{userId}", cart.getUserId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cart.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(post);
        assertEquals(cart.getUserId(), post.getUserId());
        assertEquals(new HashSet<>(cart.getItems()), new HashSet<>(post.getItems()));

        webTestClient.get()
                .uri("/api/v1/cart/{userId}", cart.getUserId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cart.class).isEqualTo(new Cart(cart.getUserId(), new ArrayList<>()));

        Order order = Order.fromByteBuffer(simpleConsumer.getAndAck().getMessage().getData().asReadOnlyByteBuffer());

        assertEquals(cart.getUserId(), order.getUserId());
        assertEquals(binding.getCartId(), order.getCartId());
        assertEquals(cart.getItems().size(), order.getEntries().size());
        for (OrderEntry entry : order.getEntries()) {
            assertTrue(cart.getItems().contains(OrderUtil.toItem(entry)));
        }
    }
}
