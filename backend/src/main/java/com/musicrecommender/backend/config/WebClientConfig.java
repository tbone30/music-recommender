package com.musicrecommender.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient spotifyWebClient(WebClient.Builder webClientBuilder, SpotifyProperties spotifyProperties) {
        return webClientBuilder
                .baseUrl(spotifyProperties.getBaseUrl())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
