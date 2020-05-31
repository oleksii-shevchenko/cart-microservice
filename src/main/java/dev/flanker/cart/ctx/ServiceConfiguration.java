package dev.flanker.cart.ctx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Random;
import java.util.function.Supplier;

@Configuration
public class ServiceConfiguration {
    @Bean
    @Scope("prototype")
    public Supplier<Long> randomIdGenerator() {
        Random random = new Random();
        return () -> Math.abs(random.nextLong());
    }
}
