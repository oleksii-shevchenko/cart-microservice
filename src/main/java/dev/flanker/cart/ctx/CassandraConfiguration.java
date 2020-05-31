package dev.flanker.cart.ctx;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
public class CassandraConfiguration {
    @Value("${cassandra.configuration.path}")
    private String configurationFile;

    @Value("${cassandra.user}")
    private String cassandraUser;

    @Value("${cassandra.password}")
    private String cassandraPassword;

    @Bean
    public CqlSession session() {
        return CqlSession.builder()
                .withConfigLoader(DriverConfigLoader.fromPath(Path.of(configurationFile)))
                .withAuthCredentials(cassandraUser, cassandraPassword)
                .build();
    }
}
