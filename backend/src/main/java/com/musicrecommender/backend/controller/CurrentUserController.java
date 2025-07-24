package com.musicrecommender.backend.controller;

import com.musicrecommender.backend.service.PlaylistService;
import com.musicrecommender.backend.dto.PlaylistListPageDTO;
import com.musicrecommender.backend.service.SpotifyIntegrationService;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Mono;


@RestController
@RequestMapping("me")
@CrossOrigin(origins = "*")
public class CurrentUserController {
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private SpotifyIntegrationService spotifyService;

    private String extractAccessToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid authorization header");
    }

    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> getUserProfile(
        @RequestHeader("Authorization") String authHeader) {
        String accessToken = extractAccessToken(authHeader);
        
        return spotifyService.getUserProfile(accessToken)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/playlists")
    public Mono<PlaylistListPageDTO> getCurrentUserPlaylists(@RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        return playlistService.getCurrentUserPlaylists(accessToken);
    }
}
