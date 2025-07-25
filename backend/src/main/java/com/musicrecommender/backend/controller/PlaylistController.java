package com.musicrecommender.backend.controller;

import com.musicrecommender.backend.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.musicrecommender.backend.entity.Playlist;
import com.musicrecommender.backend.dto.PlaylistDTO;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("playlists")
@CrossOrigin(origins = "*")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;
    private static final Logger logger = LoggerFactory.getLogger(PlaylistController.class);

    @GetMapping("/{id}")
    public Mono<PlaylistDTO> getPlaylist(@PathVariable String id) {
        return playlistService.getPlaylist(id)
            .map(playlist -> new PlaylistDTO(playlist))
            .switchIfEmpty(Mono.fromSupplier(() -> {
                logger.error("Playlist with ID {} not found", id);
                return new PlaylistDTO(); // Return an empty PlaylistDTO
            }));
    }
}
