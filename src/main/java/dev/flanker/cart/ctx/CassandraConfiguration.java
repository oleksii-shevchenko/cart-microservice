package dev.flanker.cart.ctx;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.datastax.oss.driver.api.core.CqlSession;

@Configuration
public class CassandraConfiguration {
    @Value("${cassandra.credentials}")
    private String credentials;

    @Value("${cassandra.keyspace}")
    private String keyspace;

    @Value("${cassandra.user:cart_service}")
    private String cassandraUser;

    @Value("${cassandra.password}")
    private String cassandraPassword;

    @Bean
    public CqlSession session() {
        return CqlSession.builder()
                .withAuthCredentials(cassandraUser, cassandraPassword)
                .withCloudSecureConnectBundle(Path.of(credentials))
                .withKeyspace(keyspace)
                .build();
    }
}
