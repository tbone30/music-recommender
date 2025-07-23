package com.musicrecommender.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.service.AlbumService;
import com.musicrecommender.backend.service.DTOMapperService;
import com.musicrecommender.backend.dto.AlbumDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("albums")
@CrossOrigin(origins = "*")
public class AlbumController {

    private static final Logger logger = LoggerFactory.getLogger(AlbumController.class);

    @Autowired
    private AlbumService albumService;

    @Autowired
    private DTOMapperService dtoMapperService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<AlbumDTO>> getAlbum(@PathVariable String id) {
        return albumService.getAlbum(id)
            .map(album -> {
                AlbumDTO albumDTO = dtoMapperService.albumToDTO(album);
                return ResponseEntity.ok(albumDTO);
            })
            .switchIfEmpty(Mono.fromSupplier(() -> {
                logger.error("Album with ID {} not found", id);
                return ResponseEntity.notFound().<AlbumDTO>build();
            }));
    }

    @GetMapping
    public Mono<List<AlbumDTO>> getSeveralAlbums(@RequestParam String ids) {
        return albumService.getSeveralAlbums(ids)
            .map(albums -> albums.stream()
                .map(dtoMapperService::albumToDTO)
                .collect(Collectors.toList()));
    }
}
