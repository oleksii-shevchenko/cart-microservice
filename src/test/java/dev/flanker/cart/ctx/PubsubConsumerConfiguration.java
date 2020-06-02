package dev.flanker.cart.ctx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.cloud.pubsub.v1.stub.GrpcSubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStubSettings;

import dev.flanker.cart.component.SimplePubsubConsumer;

@Configuration
public class PubsubConsumerConfiguration {
    @Value("${gcp.project}")
    private String projectName;

    @Value("${gcp.pubsub.subscription}")
    private String subscriptionName;

    @Bean
    public SubscriberStub subscriber() throws Exception {
        return GrpcSubscriberStub.create(settings());
    }

    @Bean
    public SimplePubsubConsumer simpleConsumer() throws Exception {
        return new SimplePubsubConsumer(projectName, subscriptionName, subscriber());
    }

    private SubscriberStubSettings settings() throws Exception {
        return SubscriberStubSettings.newBuilder().build();
    }
}
