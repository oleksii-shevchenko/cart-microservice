package dev.flanker.cart.component;

import com.google.pubsub.v1.ReceivedMessage;

public interface SimpleConsumer {
    ReceivedMessage get();

    void ack(String id);
}
