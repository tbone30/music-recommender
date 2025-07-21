package com.musicrecommender.backend.controller;

import com.musicrecommender.backend.service.SpotifyIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/spotify")
@CrossOrigin(origins = "*")
public class SpotifyController {
    private final SpotifyIntegrationService spotifyService;

    @Autowired
    public SpotifyController(SpotifyIntegrationService spotifyService) {
        this.spotifyService = spotifyService;
    }

     /**
     * Test endpoint to verify Spotify API connectivity
     */
    @GetMapping("/test")
    public Mono<String> testSpotifyConnection() {
        return spotifyService.getClientCredentialsToken()
            .map(token -> "Successfully connected to Spotify API. Token received: " + 
                            token.substring(0, Math.min(token.length(), 20)) + "...")
            .onErrorReturn("Failed to connect to Spotify API");
    }

    @GetMapping("/me")
    public Mono<ResponseEntity<Map<String, Object>>> getUserProfile(
            @RequestHeader("Authorization") String authHeader) {
        String accessToken = extractAccessToken(authHeader);
        
        return spotifyService.getUserProfile(accessToken)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    @GetMapping("/me/playlists")
    public Mono<ResponseEntity<Map<String, Object>>> getUserPlaylists(
            @RequestHeader("Authorization") String authHeader) {
        String accessToken = extractAccessToken(authHeader);
        
        return spotifyService.getUserPlaylists(accessToken)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    @GetMapping("/me/top/tracks")
    public Mono<ResponseEntity<Map<String, Object>>> getUserTopTracks(
            @RequestHeader("Authorization") String authHeader) {
        String accessToken = extractAccessToken(authHeader);
        
        return spotifyService.getUserTopTracks(accessToken)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    @GetMapping("/me/top/artists")
    public Mono<ResponseEntity<Map<String, Object>>> getUserTopArtists(
            @RequestHeader("Authorization") String authHeader) {
        String accessToken = extractAccessToken(authHeader);
        
        return spotifyService.getUserTopArtists(accessToken)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    @GetMapping("/tracks/{trackId}")
    public Mono<ResponseEntity<Map<String, Object>>> getTrack(
            @PathVariable String trackId,
            @RequestHeader("Authorization") String authHeader) {
        String accessToken = extractAccessToken(authHeader);
        
        return spotifyService.getTrackWithUserToken(trackId, accessToken)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    @GetMapping("/tracks")
    public Mono<ResponseEntity<List<Map<String, Object>>>> getTracks(
            @RequestParam String ids,
            @RequestHeader("Authorization") String authHeader) {
        String accessToken = extractAccessToken(authHeader);
        
        return spotifyService.getSeveralTracksWithUserToken(ids, accessToken)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    // Public endpoints (using client credentials)
    @GetMapping("/public/tracks/{trackId}")
    public Mono<ResponseEntity<Map<String, Object>>> getPublicTrack(@PathVariable String trackId) {
        return spotifyService.getTrack(trackId)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    @GetMapping("/public/artists/{artistId}")
    public Mono<ResponseEntity<Map<String, Object>>> getPublicArtist(@PathVariable String artistId) {
        return spotifyService.getArtist(artistId)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    @GetMapping("/public/albums/{albumId}")
    public Mono<ResponseEntity<Map<String, Object>>> getPublicAlbum(@PathVariable String albumId) {
        return spotifyService.getAlbum(albumId)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    private String extractAccessToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid authorization header");
    }
}
