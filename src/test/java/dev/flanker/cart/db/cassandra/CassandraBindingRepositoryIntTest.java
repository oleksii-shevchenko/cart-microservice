package dev.flanker.cart.db.cassandra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import dev.flanker.cart.ctx.CassandraConfiguration;
import dev.flanker.cart.domain.Binding;

@SpringBootTest(classes = { CassandraConfiguration.class, CassandraBindingRepository.class })
@TestPropertySource(locations = "classpath:application.properties")
@EnableConfigurationProperties
class CassandraBindingRepositoryIntTest {
    @Autowired
    private CassandraBindingRepository bindingRepository;

    @Test
    public void okTest() {
        Binding binding = new Binding(
                ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE),
                ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE)
        );

        bindingRepository.put(binding).toCompletableFuture().join();

        assertEquals(binding, bindingRepository.get(binding.getUserId()).toCompletableFuture().join());
        assertTrue(bindingRepository.delete(binding.getUserId()).toCompletableFuture().join());
        assertNull(bindingRepository.get(binding.getUserId()).toCompletableFuture().join());
    }
}
