package dev.flanker.cart.ctx;

import java.util.Random;
import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ServiceConfiguration {
    @Bean
    @Scope("prototype")
    public Supplier<Long> randomIdGenerator() {
        Random random = new Random();
        return () -> Math.abs(random.nextLong());
    }
}
