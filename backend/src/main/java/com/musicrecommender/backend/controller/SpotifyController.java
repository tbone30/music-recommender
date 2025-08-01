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
}
