package dev.flanker.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class CartMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartMicroserviceApplication.class, args);
    }

}
