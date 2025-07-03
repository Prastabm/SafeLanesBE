package com.safelanes.service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SafeLanesBEApplication {
    public static void main(String[] args) {
        SpringApplication.run(SafeLanesBEApplication.class, args);
    }

}
