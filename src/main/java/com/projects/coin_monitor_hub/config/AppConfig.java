package com.projects.coin_monitor_hub.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
    private static final String BASE_URL = "https://api.coingecko.com";

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder().baseUrl(BASE_URL);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
