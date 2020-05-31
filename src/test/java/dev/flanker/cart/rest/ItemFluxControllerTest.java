package dev.flanker.cart.rest;

import dev.flanker.cart.domain.Item;
import dev.flanker.cart.exception.NotFoundException;
import dev.flanker.cart.rest.domain.UpdateRequest;
import dev.flanker.cart.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(ItemFluxController.class)
class ItemFluxControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ItemService itemService;

    @BeforeEach
    public void noMoreInteractions() {
        Mockito.verifyNoMoreInteractions(itemService);
    }

    @Test
    public void getOkTest() {
        String itemId = "1234";
        long uuid = 4865464456L;
        Item item = new Item(itemId, 3);

        when(itemService.get(uuid, itemId)).thenReturn(CompletableFuture.completedFuture(item));

        webTestClient.get()
                .uri("/api/v1/cart/{userId}/{itemId}", uuid, itemId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class).isEqualTo(item);

        verify(itemService).get(uuid, itemId);
    }

    @Test
    public void getNotFoundTest() {
        String itemId = "1234";
        long uuid = 63465465369L;

        when(itemService.get(uuid, itemId)).thenReturn(CompletableFuture.failedFuture(new NotFoundException()));

        webTestClient.get()
                .uri("/api/v1/cart/{userId}/{itemId}", uuid, itemId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();

        verify(itemService).get(uuid, itemId);
    }

    @Test
    public void getFailTest() {
        String itemId = "1234";
        long uuid = 63465465369L;

        when(itemService.get(uuid, itemId)).thenReturn(CompletableFuture.failedFuture(new RuntimeException()));

        webTestClient.get()
                .uri("/api/v1/cart/{userId}/{itemId}", uuid, itemId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();

        verify(itemService).get(uuid, itemId);
    }

    @Test
    public void putDefaultOkTest() {
        String itemId = "1234";
        long uuid = 63465465369L;
        Item item = new Item(itemId, 1);

        when(itemService.put(uuid, item)).thenReturn(CompletableFuture.completedFuture(null));

        webTestClient.put()
                .uri("/api/v1/cart/{userId}/{itemId}", uuid, itemId)
                .exchange()
                .expectStatus().isOk();

        verify(itemService).put(uuid, item);
    }

    @Test
    public void putOkTest() {
        String itemId = "123";
        long uuid = 63465465369L;
        Item item = new Item(itemId, 6);

        when(itemService.put(uuid, item)).thenReturn(CompletableFuture.completedFuture(null));

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/cart/{userId}/{itemId}")
                        .queryParam("number", item.getNumber())
                        .build(uuid, item.getItemId()))
                .exchange()
                .expectStatus().isOk();

        verify(itemService).put(uuid, item);
    }

    @Test
    public void putFailTest() {
        String itemId = "1234";
        long uuid = 63465465369L;
        Item item = new Item(itemId, 1);

        when(itemService.put(uuid, item)).thenReturn(CompletableFuture.failedFuture(new RuntimeException()));

        webTestClient.put()
                .uri("/api/v1/cart/{userId}/{itemId}", uuid, itemId)
                .exchange()
                .expectStatus().is5xxServerError();

        verify(itemService).put(uuid, item);
    }

    @Test
    public void deleteOkTest() {
        String itemId = "1234";
        long uuid = 63465465369L;

        when(itemService.delete(uuid, itemId)).thenReturn(CompletableFuture.completedFuture(true));

        webTestClient.delete()
                .uri("/api/v1/cart/{userId}/{itemId}", uuid, itemId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class).isEqualTo(true);

        verify(itemService).delete(uuid, itemId);
    }

    @Test
    public void deleteFailTest() {
        String itemId = "1234";
        long uuid = 63465465369L;

        when(itemService.delete(uuid, itemId)).thenReturn(CompletableFuture.failedFuture(new RuntimeException()));

        webTestClient.delete()
                .uri("/api/v1/cart/{userId}/{itemId}", uuid, itemId)
                .exchange()
                .expectStatus().is5xxServerError();

        verify(itemService).delete(uuid, itemId);
    }

    @Test
    public void updateIncreaseOkTest() {
        String itemId = "1234";
        long uuid = 63465465369L;
        UpdateRequest updateRequest = new UpdateRequest(UpdateRequest.Operation.INCREMENT, 3);
        Item item = new Item(itemId, updateRequest.getNumber());
        Item updated = new Item(itemId, 6);

        when(itemService.increase(uuid, item)).thenReturn(CompletableFuture.completedFuture(updated));

        webTestClient.post()
                .uri("/api/v1/cart/{userId}/{itemId}", uuid, itemId)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class).isEqualTo(updated);

        verify(itemService).increase(uuid, item);
    }

    @Test
    public void updateDecreaseOkTest() {
        String itemId = "1234";
        long uuid = 63465465369L;
        UpdateRequest updateRequest = new UpdateRequest(UpdateRequest.Operation.DECREMENT, 3);
        Item item = new Item(itemId, updateRequest.getNumber());
        Item updated = new Item(itemId, 6);

        when(itemService.decrease(uuid, item)).thenReturn(CompletableFuture.completedFuture(updated));

        webTestClient.post()
                .uri("/api/v1/cart/{userId}/{itemId}", uuid, itemId)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class).isEqualTo(updated);

        verify(itemService).decrease(uuid, item);
    }

    @Test
    public void updateNotFoundTest() {
        String itemId = "1234";
        long uuid = 63465465369L;
        UpdateRequest updateRequest = new UpdateRequest(UpdateRequest.Operation.DECREMENT, 3);
        Item item = new Item(itemId, updateRequest.getNumber());

        when(itemService.decrease(uuid, item)).thenReturn(CompletableFuture.failedFuture(new NotFoundException()));

        webTestClient.post()
                .uri("/api/v1/cart/{userId}/{itemId}", uuid, itemId)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isNotFound();

        verify(itemService).decrease(uuid, item);
    }

    @Test
    public void updateEmptyRequestTest() {
        String itemId = "1234";
        long uuid = 63465465369L;

        webTestClient.post()
                .uri("/api/v1/cart/{userId}/{itemId}", uuid, itemId)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void updateNoOperationTest() {
        String itemId = "1234";
        long uuid = 63465465369L;
        UpdateRequest updateRequest = new UpdateRequest(null, 3);

        webTestClient.post()
                .uri("/api/v1/cart/{userId}/{itemId}", uuid, itemId)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void updateBadQuantityTest() {
        String itemId = "1234";
        long uuid = 63465465369L;
        UpdateRequest updateRequest = new UpdateRequest(UpdateRequest.Operation.INCREMENT, -1);

        webTestClient.post()
                .uri("/api/v1/cart/{userId}/{itemId}", uuid, itemId)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
