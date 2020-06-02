package dev.flanker.cart.ctx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.PublisherInterface;
import com.google.pubsub.v1.TopicName;

@Configuration
public class PubsubConfiguration {
    @Value("${gcp.project}")
    private String projectName;

    @Value("${gcp.pubsub.topic}")
    private String topicName;

    @Bean
    public PublisherInterface publisher() throws Exception {
        return Publisher.newBuilder(TopicName.of(projectName, topicName)).build();
    }
}
