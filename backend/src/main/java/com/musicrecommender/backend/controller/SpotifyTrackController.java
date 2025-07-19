package com.musicrecommender.backend.controller;

import com.musicrecommender.backend.service.TrackService;
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
    private TrackService trackService;

    @GetMapping("/{id}")
    public String getTrackById(@PathVariable String id) {
        return trackService.getTrack(id);
    }

    @GetMapping
    public String getSeveralTracks(@RequestParam String ids) {
        return trackService.getSeveralTracks(ids);
    }
}
