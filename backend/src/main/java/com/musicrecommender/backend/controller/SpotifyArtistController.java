package com.musicrecommender.backend.controller;

import com.musicrecommender.backend.service.SpotifyIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.musicrecommender.backend.entity.Artist;
import java.util.List;

import java.util.Map;

@RestController
@RequestMapping("/spotify/artists")
// @CrossOrigin(origins = "http://localhost:3000")
public class SpotifyArtistController {
    private final SpotifyIntegrationService spotifyService;

    @Autowired
    public SpotifyArtistController(SpotifyIntegrationService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/{artistId}")
    public Mono<String> getArtist(@PathVariable String artistId) {
        return spotifyService.getArtist(artistId).map(Artist::toString);
    }

    @GetMapping
    public Mono<List<Artist>> getSeveralArtists(@RequestParam String ids) {
        return spotifyService.getSeveralArtists(ids);
    }
}
