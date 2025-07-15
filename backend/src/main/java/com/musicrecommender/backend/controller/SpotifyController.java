package com.musicrecommender.backend.controller;

import com.musicrecommender.backend.service.SpotifyIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/spotify")
@CrossOrigin(origins = "http://localhost:3000")
public class SpotifyController {
    private final SpotifyIntegrationService spotifyService;

    @Autowired
    public SpotifyController(SpotifyIntegrationService spotifyService) {
        this.spotifyService = spotifyService;
    }
}
