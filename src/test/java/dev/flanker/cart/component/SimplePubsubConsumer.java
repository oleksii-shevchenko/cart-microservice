package dev.flanker.cart.component;

import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.pubsub.v1.AcknowledgeRequest;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PullRequest;
import com.google.pubsub.v1.PullResponse;
import com.google.pubsub.v1.ReceivedMessage;

public class SimplePubsubConsumer implements SimpleConsumer {
    private static final int MAX_MESSAGES = 1;

    private final String subscription;

    private final SubscriberStub subscriber;

    public SimplePubsubConsumer(String projectName, String subscriptionName, SubscriberStub subscriber) {
        this.subscriber = subscriber;
        this.subscription = ProjectSubscriptionName.format(projectName, subscriptionName);
    }

    @Override
    public ReceivedMessage get() {
        PullResponse response = subscriber.pullCallable().call(createPullRequest());
        if (response.getReceivedMessagesCount() != 1) {
            throw new IllegalStateException();
        }
        return response.getReceivedMessages(0);
    }

    @Override
    public void ack(String id) {
        subscriber.acknowledgeCallable().call(createAcknowledgeRequest(id));
    }

    private PullRequest createPullRequest() {
        return PullRequest.newBuilder()
                .setMaxMessages(MAX_MESSAGES)
                .setSubscription(subscription)
                .build();
    }

    private AcknowledgeRequest createAcknowledgeRequest(String id) {
        return AcknowledgeRequest.newBuilder()
                .setSubscription(subscription)
                .addAckIds(id)
                .build();
    }
}
