package com.musicrecommender.backend.service;

import com.musicrecommender.backend.config.SpotifyProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.time.Duration;

@Service
public class SpotifyIntegrationService {
    private final WebClient spotifyWebClient;
    private final SpotifyProperties spotifyProperties;
    private Mono<String> cachedToken;

    private static final Logger logger = LoggerFactory.getLogger(SpotifyIntegrationService.class);

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

    private Mono<List<Map<String, Object>>> fetchAllPagesFromUrl(String nextUrl, List<Map<String, Object>> allObjects) {
        return getValidToken()
            .flatMap(token -> {
                // Extract the path and query from the full URL
                String path = nextUrl.replace("https://api.spotify.com/v1", "");
                
                return spotifyWebClient.get()
                    .uri(path)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .flatMap(response -> {
                        // Add current page
                        List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
                        if (items != null && !items.isEmpty()) {
                            allObjects.addAll(items);
                        }
                        
                        // Check if there are more pages
                        String next = (String) response.get("next");
                        if (next != null) {
                            // Recursively fetch the next page
                            return fetchAllPagesFromUrl(next, allObjects);
                        } else {
                            // All pages fetched
                            return Mono.just(allObjects);
                        }
                    });
            });
    }

    // ALBUM METHODS

    public Mono<Map<String, Object>> getAlbum(String albumId) {
        return getValidToken()
            .flatMap(token -> spotifyWebClient.get()
                .uri("/albums/{id}", albumId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}));
    }

    public Mono<List<Map<String, Object>>> getSeveralAlbums(String ids) {
        return getValidToken()
        .flatMap(token -> spotifyWebClient.get()
            .uri("/albums?ids={ids}", ids)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> (List<Map<String, Object>>) response.get("albums")));
    }

    public Mono<List<Map<String, Object>>> fetchAllTracksForAlbum(Map<String, Object> initialTracks) {
        return fetchAllPagesFromUrl((String) initialTracks.get("next"), (List<Map<String, Object>>) initialTracks.get("items"));
    }

    //ARTIST METHODS

    public Mono<Map<String, Object>> getArtist(String artistId) {
        return getValidToken()
            .flatMap(token -> spotifyWebClient.get()
                .uri("/artists/{id}", artistId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}));
    }

    public Mono<List<Map<String, Object>>> getSeveralArtists(String ids) {
        return getValidToken()
        .flatMap(token -> spotifyWebClient.get()
            .uri("/artists?ids={ids}", ids)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> (List<Map<String, Object>>) response.get("artists")));
    }

    public Mono<Map<String, Object>> getArtistAlbums(String artistId) {
        return getValidToken()
            .flatMap(token -> spotifyWebClient.get()
                .uri("/artists/{id}/albums", artistId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}));
    }

    public Mono<List<Map<String, Object>>> fetchAllAlbumsForArtist(Map<String, Object> initialAlbums) {
        return fetchAllPagesFromUrl((String) initialAlbums.get("next"), (List<Map<String, Object>>) initialAlbums.get("items"));
    }

    public Mono<List<Map<String, Object>>> getArtistTopTracks(String artistId) {
        return getValidToken()
            .flatMap(token -> spotifyWebClient.get()
                .uri("/artists/{id}/top-tracks", artistId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {}));
    }

    // TRACK METHODS

    public Mono<Map<String, Object>> getTrack(String trackId) {
        return getValidToken()
            .flatMap(token -> spotifyWebClient.get()
                .uri("/tracks/{id}", trackId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}));
    }

    // USER-SPECIFIC TRACK METHODS (using user's access token)
    
    public Mono<Map<String, Object>> getTrackWithUserToken(String trackId, String accessToken) {
        return WebClient.create(spotifyProperties.getBaseUrl())
            .get()
            .uri("/tracks/{id}", trackId)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    @SuppressWarnings("unchecked")
    public Mono<List<Map<String, Object>>> getSeveralTracksWithUserToken(String ids, String accessToken) {
        return WebClient.create(spotifyProperties.getBaseUrl())
            .get()
            .uri("/tracks?ids={ids}", ids)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> (List<Map<String, Object>>) response.get("tracks"));
    }

    public Mono<Map<String, Object>> getUserProfile(String accessToken) {
        return WebClient.create(spotifyProperties.getBaseUrl())
            .get()
            .uri("/me")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Mono<Map<String, Object>> getUserPlaylists(String accessToken) {
        return WebClient.create(spotifyProperties.getBaseUrl())
            .get()
            .uri("/me/playlists")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Mono<Map<String, Object>> getUserTopTracks(String accessToken) {
        return WebClient.create(spotifyProperties.getBaseUrl())
            .get()
            .uri("/me/top/tracks")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Mono<Map<String, Object>> getUserTopArtists(String accessToken) {
        return WebClient.create(spotifyProperties.getBaseUrl())
            .get()
            .uri("/me/top/artists")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    @SuppressWarnings("unchecked")
    public Mono<List<Map<String, Object>>> getSeveralTracks(String ids) {
    return getValidToken()
        .flatMap(token -> spotifyWebClient.get()
            .uri("/tracks?ids={ids}", ids)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> (List<Map<String, Object>>) response.get("tracks")));
    }
}   
