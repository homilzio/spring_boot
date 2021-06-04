package com.training.springbootbuyitem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;

@EnableDiscoveryClient
@SpringBootApplication()
@PropertySource("classpath:bootstrap.properties")
public class BuyItemApplication {

        public static void main(String[] args) {
            SpringApplication.run(BuyItemApplication.class, args);
        }

}
