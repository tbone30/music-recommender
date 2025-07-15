package com.musicrecommender.backend.service;

import com.musicrecommender.backend.config.SpotifyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Map;

public class SpotifyIntegrationService {
    private final WebClient spotifyWebClient;
    private final SpotifyProperties spotifyProperties;

    @Autowired
    public SpotifyIntegrationService(WebClient spotifyWebClient, SpotifyProperties spotifyProperties) {
        this.spotifyWebClient = spotifyWebClient;
        this.spotifyProperties = spotifyProperties;
    }

        public Mono<String> getClientCredentialsToken() {
        String credentials = spotifyProperties.getClientId() + ":" + spotifyProperties.getClientSecret();
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        return WebClient.create(spotifyProperties.getAccountsUrl())
                .post()
                .uri("/api/token")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .bodyValue("grant_type=client_credentials")
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("access_token"));
    }
}
