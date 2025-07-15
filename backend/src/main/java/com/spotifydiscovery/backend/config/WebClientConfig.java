package com.spotifydiscovery.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

/**
 * Web Client Configuration for External API Calls
 * 
 * Configures WebClient beans for:
 * - Spotify Web API integration
 * - HTTP client settings
 * - Request/Response logging
 * 
 * @author Spotify Discovery Team
 */
@Configuration
public class WebClientConfig {

    @Value("${spotify.api.base-url}")
    private String spotifyApiBaseUrl;

    @Value("${spotify.api.accounts-url}")
    private String spotifyAccountsUrl;

    /**
     * WebClient for Spotify Web API calls
     */
    @Bean("spotifyApiWebClient")
    public WebClient spotifyApiWebClient() {
        return WebClient.builder()
            .baseUrl(spotifyApiBaseUrl)
            .defaultHeader("Content-Type", "application/json")
            .defaultHeader("Accept", "application/json")
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024)) // 1MB
            .build();
    }

    /**
     * WebClient for Spotify Accounts API (OAuth)
     */
    @Bean("spotifyAccountsWebClient")
    public WebClient spotifyAccountsWebClient() {
        return WebClient.builder()
            .baseUrl(spotifyAccountsUrl)
            .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
            .defaultHeader("Accept", "application/json")
            .build();
    }

    /**
     * General purpose WebClient for other external APIs
     */
    @Bean("generalWebClient")
    public WebClient generalWebClient() {
        return WebClient.builder()
            .defaultHeader("Content-Type", "application/json")
            .defaultHeader("Accept", "application/json")
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024)) // 2MB
            .build();
    }
}
