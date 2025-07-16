package com.musicrecommender.backend.service;

import com.musicrecommender.backend.entity.Artist;
import com.musicrecommender.backend.entity.SpotifyImage;
import com.musicrecommender.backend.config.SpotifyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Mono.*;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.time.Duration;

@Service
public class SpotifyIntegrationService {
    private final WebClient spotifyWebClient;
    private final SpotifyProperties spotifyProperties;
    private Mono<String> cachedToken;

    @Autowired
    public SpotifyIntegrationService(WebClient spotifyWebClient, SpotifyProperties spotifyProperties) {
        this.spotifyWebClient = spotifyWebClient;
        this.spotifyProperties = spotifyProperties;
    }

    public Mono<String> getClientCredentialsToken() {
        String requestBody = "grant_type=client_credentials&client_id=" + 
                            spotifyProperties.getClientId() + 
                            "&client_secret=" + 
                            spotifyProperties.getClientSecret();

        System.out.println("=== TOKEN REQUEST DEBUG ===");
        System.out.println("Accounts URL: " + spotifyProperties.getAccountsUrl());
        System.out.println("Full URL: " + spotifyProperties.getAccountsUrl() + "/api/token");
        System.out.println("Request body: " + requestBody);
        System.out.println("============================");

        return WebClient.create(spotifyProperties.getAccountsUrl())
            .post()
            .uri("/api/token")
            .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
            .bodyValue(requestBody)
            .retrieve()
            .onStatus(status -> !status.is2xxSuccessful(),
                response -> {
                    System.out.println("Token error status: " + response.statusCode());
                    return response.bodyToMono(String.class)
                            .doOnNext(body -> System.out.println("Error body: " + body))
                            .then(Mono.error(new RuntimeException("Token request failed")));
            })
            .bodyToMono(Map.class)
            .doOnNext(response -> System.out.println("Token response: " + response))
            .map(response -> (String) response.get("access_token"));
    }

    private Mono<String> getValidToken() {
        if (cachedToken == null) {
            cachedToken = getClientCredentialsToken().cache(Duration.ofMinutes(55)); // Spotify tokens last 1 hour
        }
        return cachedToken;
    }

    // Get artist from Spotify by ID
    public Mono<Artist> getArtist(String artistId) {
        return getValidToken()
            .flatMap(token -> spotifyWebClient.get()
                .uri("/artists/{id}", artistId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    Artist artist = new Artist(
                        (String) response.get("id"),
                        (String) response.get("href"),
                        (String) response.get("name"),
                        (Integer) ((Map<String, Object>) response.get("followers")).get("total"),
                        (Integer) response.get("popularity"),
                        (List<SpotifyImage>) response.get("images"),
                        (String) response.get("uri"),
                        (List<String>) response.get("genres")
                    );
                    return artist;
                }));
    }

    // TODO: fix image translation
    public Mono<List<Artist>> getSeveralArtists(String ids) {
        return getValidToken()
            .flatMap(token -> spotifyWebClient.get()
                .uri("/artists?ids={ids}", ids)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, Object>> artistsData = (List<Map<String, Object>>) response.get("artists");
                    List<Artist> artists = artistsData.stream().map(data -> {
                        return new Artist(
                            (String) data.get("id"),
                            (String) data.get("href"),
                            (String) data.get("name"),
                            (Integer) ((Map<String, Object>) data.get("followers")).get("total"),
                            (Integer) data.get("popularity"),
                            (List<SpotifyImage>) data.get("images"),
                            (String) data.get("uri"),
                            (List<String>) data.get("genres")
                        );
                    }).toList();
                    return artists;
                }));
    }
}
