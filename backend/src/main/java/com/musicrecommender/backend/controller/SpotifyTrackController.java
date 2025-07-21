package com.musicrecommender.backend.controller;

import com.musicrecommender.backend.entity.Track;
import com.musicrecommender.backend.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("tracks")
@CrossOrigin(origins = "*")
public class SpotifyTrackController {

    @Autowired
    private TrackService trackService;

    @GetMapping("/{id}")
    public Mono<Track> getTrackById(@PathVariable String id) {
        return trackService.getTrack(id);
    }

    @GetMapping
    public Mono<List<Track>> getSeveralTracks(@RequestParam String ids) {
        return trackService.getSeveralTracks(ids);
    }
}
