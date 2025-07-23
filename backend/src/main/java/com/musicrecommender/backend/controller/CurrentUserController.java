package com.musicrecommender.backend.controller;

import com.musicrecommender.backend.service.PlaylistService;
import com.musicrecommender.backend.dto.PlaylistListPageDTO;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.core.publisher.Mono;


@RestController
@RequestMapping("me")
@CrossOrigin(origins = "*")
public class CurrentUserController {
    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/playlists")
    public Mono<PlaylistListPageDTO> getCurrentUserPlaylists(@RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        return playlistService.getCurrentUserPlaylists(accessToken);
    }
}
