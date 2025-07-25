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
    public Mono<PlaylistDTO> getPlaylist(
            @PathVariable String id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String accessToken = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            accessToken = authorizationHeader.substring(7);
        }
        Mono<Playlist> playlistMono;
        if (accessToken != null && !accessToken.isEmpty()) {
            playlistMono = playlistService.getPlaylist(id, accessToken);
        } else {
            playlistMono = playlistService.getPlaylist(id);
        }
        return playlistMono
            .map(playlist -> new PlaylistDTO(playlist))
            .switchIfEmpty(Mono.fromSupplier(() -> {
                logger.error("Playlist with ID {} not found or not accessible", id);
                return new PlaylistDTO();
            }));
    }
}
