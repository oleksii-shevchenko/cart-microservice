package dev.flanker.cart.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.google.api.core.ApiFuture;
import com.spotify.futures.CompletableFuturesExtra;

public final class AsyncUtil {
    private AsyncUtil() { }

    public static boolean isCompletedExceptionally(CompletionStage<?> stage) {
        return stage.toCompletableFuture().isCompletedExceptionally();
    }

    public static <V> ApiFuture<V> failedFuture() {
        return failedFuture(new RuntimeException("Test exception"));
    }

    public static <V> ApiFuture<V> failedFuture(Throwable t) {
        return CompletableFuturesExtra.toApiFuture(CompletableFuturesExtra.exceptionallyCompletedFuture(t));
    }

    public static <V> ApiFuture<V> completedFuture(V value) {
        return CompletableFuturesExtra.toApiFuture(CompletableFuture.completedFuture(value));
    }
}
