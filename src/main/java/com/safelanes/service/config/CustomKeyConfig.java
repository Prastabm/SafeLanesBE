package com.safelanes.service.config;

import com.safelanes.service.dto.Coordinate;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomKeyConfig {
    @Bean("customKeyGenerator")
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            Coordinate source = (Coordinate) params[0];
            Coordinate destination = (Coordinate) params[1];
            return "[(" + source.getLat() + "," + source.getLon() + ")-(" +
                    destination.getLat() + "," + destination.getLon() + ")]";
        };
    }
}