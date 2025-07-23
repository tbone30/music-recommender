package com.musicrecommender.backend.controller;

import com.musicrecommender.backend.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.musicrecommender.backend.entity.Playlist;
import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.entity.Track;
import java.util.List;

import java.util.Map;

@RestController
@RequestMapping("playlists")
@CrossOrigin(origins = "*")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/{playlistId}")
    public Mono<Playlist> getPlaylist(@PathVariable String playlistId) {
        return playlistService.getPlaylist(playlistId);
    }
}
