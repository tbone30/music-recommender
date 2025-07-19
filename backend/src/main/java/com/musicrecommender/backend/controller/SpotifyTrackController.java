package com.musicrecommender.backend.controller;

import com.musicrecommender.backend.service.SpotifyIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/tracks")
public class SpotifyTrackController {

    @Autowired
    private SpotifyIntegrationService spotifyIntegrationService;

    @GetMapping("/{id}")
    public String getTrackById(@PathVariable String id) {
        return spotifyIntegrationService.getTrack(id)
            .map(track -> track.toString())
            .defaultIfEmpty("Track not found")
            .block();
    }

    @GetMapping
    public String getSeveralTracks(@RequestParam String ids) {
        return spotifyIntegrationService.getSeveralTracks(ids)
            .map(tracks -> tracks.toString())
            .defaultIfEmpty("No tracks found")
            .block();
    }
}
