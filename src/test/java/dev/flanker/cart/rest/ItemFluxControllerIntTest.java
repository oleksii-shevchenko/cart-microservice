package dev.flanker.cart.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import dev.flanker.cart.CartMicroserviceApplication;
import dev.flanker.cart.domain.Item;
import dev.flanker.cart.rest.domain.UpdateRequest;
import dev.flanker.cart.util.RandomUtil;

@SpringBootTest(classes = CartMicroserviceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ItemFluxControllerIntTest {
    private final WebTestClient webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:8080")
            .build();

    @Test
    public void okTest() {
        long userId = RandomUtil.userId();
        Item item = RandomUtil.item();

        webTestClient.get()
                .uri("/api/v1/cart/{userId}/{itemId}", userId, item.getItemId())
                .exchange()
                .expectStatus().isNotFound();

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/cart/{userId}/{itemId}")
                        .queryParam("number", item.getNumber())
                        .build(userId, item.getItemId()))
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri("/api/v1/cart/{userId}/{itemId}", userId, item.getItemId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class).isEqualTo(item);

        int incr = 4;
        Item incremented = new Item(item.getItemId(), item.getNumber() + incr);

        webTestClient.post()
                .uri("/api/v1/cart/{userId}/{itemId}", userId, item.getItemId())
                .bodyValue(new UpdateRequest(UpdateRequest.Operation.INCREMENT, incr))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class).isEqualTo(incremented);

        webTestClient.get()
                .uri("/api/v1/cart/{userId}/{itemId}", userId, item.getItemId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class).isEqualTo(incremented);

        int decr = 2;
        Item decremented = new Item(item.getItemId(), incremented.getNumber() - decr);

        webTestClient.post()
                .uri("/api/v1/cart/{userId}/{itemId}", userId, item.getItemId())
                .bodyValue(new UpdateRequest(UpdateRequest.Operation.DECREMENT, decr))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class).isEqualTo(decremented);

        webTestClient.get()
                .uri("/api/v1/cart/{userId}/{itemId}", userId, item.getItemId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class).isEqualTo(decremented);

        webTestClient.delete()
                .uri("/api/v1/cart/{userId}/{itemId}", userId, item.getItemId())
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri("/api/v1/cart/{userId}/{itemId}", userId, item.getItemId())
                .exchange()
                .expectStatus().isNotFound();
    }
}
