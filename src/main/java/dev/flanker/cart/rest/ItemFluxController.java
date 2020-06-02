package dev.flanker.cart.rest;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import dev.flanker.cart.domain.Item;
import dev.flanker.cart.exception.NotFoundException;
import dev.flanker.cart.rest.domain.UpdateRequest;
import dev.flanker.cart.service.ItemService;
import reactor.core.publisher.Mono;

@RestController
public class ItemFluxController {
    private static final String DEFAULT_NUMBER = "1";

    private final ItemService itemService;

    @Autowired
    public ItemFluxController(ItemService itemService) {
        this.itemService = itemService;
    }

    @ResponseBody
    @GetMapping("/api/v1/cart/{userId}/{itemId}")
    public Mono<? extends Item> getItem(@PathVariable long userId,
                                        @PathVariable String itemId) {
        CompletionStage<Item> itemOrNotFound = itemService.get(userId, itemId)
                .handle((item, thr) -> {
                    if (thr != null) {
                        throw new RuntimeException(thr);
                    } else {
                        if (item != null) {
                            return item;
                        } else {
                            throw new NotFoundException();
                        }
                    }
                });

        return Mono.fromCompletionStage(itemOrNotFound);
    }

    @ResponseBody
    @PostMapping("/api/v1/cart/{userId}/{itemId}")
    public Mono<Item> updateItem(@PathVariable long userId,
                                 @PathVariable String itemId,
                                 @RequestBody UpdateRequest update) {
        if (update == null || update.getOperation() == null || update.getNumber() <= 0) {
            return Mono.error(badUpdateRequest());
        }

        CompletionStage<Item> updateFuture;
        switch (update.getOperation()) {
            case DECREMENT:
                updateFuture = itemService.decrease(userId, new Item(itemId, update.getNumber()));
                break;
            case INCREMENT:
                updateFuture = itemService.increase(userId, new Item(itemId, update.getNumber()));
                break;
            default:
                updateFuture = CompletableFuture.failedFuture(badUpdateRequest());
        }

        return Mono.fromCompletionStage(updateFuture);
    }

    @ResponseBody
    @PutMapping("/api/v1/cart/{userId}/{itemId}")
    public Mono<Void> putItem(@PathVariable long userId,
                              @PathVariable String itemId,
                              @RequestParam(value = "number", defaultValue = DEFAULT_NUMBER) int number) {
        return Mono.fromCompletionStage(itemService.put(userId, new Item(itemId, number)));
    }

    @ResponseBody
    @DeleteMapping("/api/v1/cart/{userId}/{itemId}")
    public Mono<Boolean> deleteItem(@PathVariable long userId,
                                    @PathVariable String itemId) {
        return Mono.fromCompletionStage(itemService.delete(userId, itemId));
    }

    private static Throwable badUpdateRequest() {
        return WebClientResponseException.create(
                HttpStatus.BAD_REQUEST.value(),
                "Update operation is incorrect",
                null,
                null,
                StandardCharsets.UTF_8);
    }
}
