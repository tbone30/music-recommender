package com.musicrecommender.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.musicrecommender.backend.service.SpotifyIntegrationService;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/spotify")
@CrossOrigin(origins = "*")
public class SpotifyApiController {
    
    @Autowired
    private SpotifyIntegrationService spotifyIntegrationService;
    
    @GetMapping("/me")
    public Mono<ResponseEntity<Map<String, Object>>> getUserProfile(
            @RequestHeader("Authorization") String authHeader) {
        String accessToken = extractAccessToken(authHeader);
        
        return spotifyIntegrationService.getUserProfile(accessToken)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    @GetMapping("/me/playlists")
    public Mono<ResponseEntity<Map<String, Object>>> getUserPlaylists(
            @RequestHeader("Authorization") String authHeader) {
        String accessToken = extractAccessToken(authHeader);
        
        return spotifyIntegrationService.getUserPlaylists(accessToken)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    @GetMapping("/me/top/tracks")
    public Mono<ResponseEntity<Map<String, Object>>> getUserTopTracks(
            @RequestHeader("Authorization") String authHeader) {
        String accessToken = extractAccessToken(authHeader);
        
        return spotifyIntegrationService.getUserTopTracks(accessToken)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    @GetMapping("/me/top/artists")
    public Mono<ResponseEntity<Map<String, Object>>> getUserTopArtists(
            @RequestHeader("Authorization") String authHeader) {
        String accessToken = extractAccessToken(authHeader);
        
        return spotifyIntegrationService.getUserTopArtists(accessToken)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    @GetMapping("/tracks/{trackId}")
    public Mono<ResponseEntity<Map<String, Object>>> getTrack(
            @PathVariable String trackId,
            @RequestHeader("Authorization") String authHeader) {
        String accessToken = extractAccessToken(authHeader);
        
        return spotifyIntegrationService.getTrackWithUserToken(trackId, accessToken)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    @GetMapping("/tracks")
    public Mono<ResponseEntity<List<Map<String, Object>>>> getTracks(
            @RequestParam String ids,
            @RequestHeader("Authorization") String authHeader) {
        String accessToken = extractAccessToken(authHeader);
        
        return spotifyIntegrationService.getSeveralTracksWithUserToken(ids, accessToken)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    // Public endpoints (using client credentials)
    @GetMapping("/public/tracks/{trackId}")
    public Mono<ResponseEntity<Map<String, Object>>> getPublicTrack(@PathVariable String trackId) {
        return spotifyIntegrationService.getTrack(trackId)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    @GetMapping("/public/artists/{artistId}")
    public Mono<ResponseEntity<Map<String, Object>>> getPublicArtist(@PathVariable String artistId) {
        return spotifyIntegrationService.getArtist(artistId)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    @GetMapping("/public/albums/{albumId}")
    public Mono<ResponseEntity<Map<String, Object>>> getPublicAlbum(@PathVariable String albumId) {
        return spotifyIntegrationService.getAlbum(albumId)
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
