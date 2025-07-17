package com.musicrecommender.backend.controller;

import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.service.SpotifyIntegrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/albums")
public class SpotifyAlbumController {
    @Autowired
    private SpotifyIntegrationService spotifyIntegrationService;

    @GetMapping("/{id}")
    public Mono<Album> getAlbum(@PathVariable String id) {
        return spotifyIntegrationService.getAlbum(id);
    }
}
