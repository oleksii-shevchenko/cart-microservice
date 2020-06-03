package dev.flanker.cart.ctx;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;

@Configuration
public class CassandraConfiguration {
    private static final String CASSANDRA_CONFIG = "cassandra/driver.conf";

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
                .withConfigLoader(DriverConfigLoader.fromClasspath(CASSANDRA_CONFIG))
                .withCloudSecureConnectBundle(Path.of(credentials))
                .withKeyspace(keyspace)
                .build();
    }
}
