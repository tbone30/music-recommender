package com.musicrecommender.backend.service;

import com.musicrecommender.backend.entity.Artist;
import com.musicrecommender.backend.entity.SpotifyImage;
import com.musicrecommender.backend.config.SpotifyProperties;
import com.musicrecommender.backend.entity.Track;
import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.service.AlbumService;
import com.musicrecommender.backend.service.TrackService;
import com.musicrecommender.backend.service.ArtistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Mono.*;

import java.util.Base64;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.time.Duration;

@Service
public class SpotifyIntegrationService {
    private final WebClient spotifyWebClient;
    private final SpotifyProperties spotifyProperties;
    private Mono<String> cachedToken;

    @Autowired
    private AlbumService albumService;
    @Autowired
    private TrackService trackService;
    @Autowired
    private ArtistService artistService;

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

    //ARTIST METHODS

    // Get artist from Spotify by ID
    public Mono<Artist> getArtist(String artistId) {
        return getValidToken()
            .flatMap(token -> spotifyWebClient.get()
                .uri("/artists/{id}", artistId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    return artistService.createArtistFromJSON((Map<String, Object>) response);
                }));
    }

    public Mono<List<Artist>> getSeveralArtists(String ids) {
        return getValidToken()
            .flatMap(token -> spotifyWebClient.get()
                .uri("/artists?ids={ids}", ids)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, Object>> artistsData = (List<Map<String, Object>>) response.get("artists");
                    return artistService.createArtistListFromJSON(artistsData);
                }));
    }

    public Mono<List<Album>> getArtistAlbums(String artistId) {
        return getValidToken()
            .flatMap(token -> spotifyWebClient.get()
                .uri("/artists/{id}/albums", artistId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    return albumService.createAlbumListFromJSON((List<Map<String, Object>>) response.get("items"));
                }));
    }

    public Mono<List<Track>> getArtistTopTracks(String artistId) {
        return getValidToken()
            .flatMap(token -> spotifyWebClient.get()
                .uri("/artists/{id}/top-tracks", artistId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    return trackService.createTrackListFromJSON((List<Map<String, Object>>) response.get("tracks"));
                }));
    }
}
