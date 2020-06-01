package dev.flanker.cart.ctx;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.nio.file.Path;

@Configuration
public class CassandraConfiguration {
    @Value("${cassandra.seed.host}")
    private String seed;

    @Value("${cassandra.seed.port:9042}")
    private int port;

    @Value("${cassandra.keyspace}")
    private String keyspace;

    @Value("${cassandra.dc:us-east1}")
    private String localDataCenter;

    // The properties below need to be resolved throwout environmental variables

    @Value("${cassandra.configuration.path:./devops/cassandra/driver/cassandra.conf}")
    private String configurationFile;

    @Value("${cassandra.user:cassandra}")
    private String cassandraUser;

    @Value("${cassandra.password}")
    private String cassandraPassword;

    @Bean
    public CqlSession session() {
        return CqlSession.builder()
                .withConfigLoader(DriverConfigLoader.fromPath(Path.of(configurationFile)))
                .withAuthCredentials(cassandraUser, cassandraPassword)
                .addContactPoint(InetSocketAddress.createUnresolved(seed, port))
                .withLocalDatacenter(localDataCenter)
                .withKeyspace(keyspace)
                .build();
    }
}
