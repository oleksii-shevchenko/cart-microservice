package dev.flanker.cart.ctx;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.PublisherInterface;
import com.google.pubsub.v1.TopicName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PubsubConfiguration {
    @Value("${gcp.project}")
    private String projectName;

    @Value("${gpc.pubsub.topic}")
    private String topicName;

    @Bean
    public PublisherInterface publisher() throws Exception {
        return Publisher.newBuilder(TopicName.of(projectName, topicName)).build();
    }
}
