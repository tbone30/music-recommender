package com.musicrecommender.backend.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.repository.AlbumRepository;
import com.musicrecommender.backend.factory.AlbumFactory;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private AlbumFactory albumFactory;

    @Autowired
    @Lazy
    private SpotifyIntegrationService spotifyIntegrationService;

    public Mono<Album> getAlbumById(String id) {
        return Mono.fromCallable(() -> albumRepository.findById(id).orElse(null));
    }

    public Mono<Album> createAlbumFromJSON(Map<String, Object> albumData) {
        String albumId = (String) albumData.get("id");
        return Mono.fromCallable(() -> albumRepository.findById(albumId))
            .flatMap(repositoryResponse -> {
                if (repositoryResponse.isPresent()) {
                    return Mono.just(repositoryResponse.get());
                } else {
                    return albumFactory.createAlbumFromJSON(albumData)
                        .flatMap(album -> Mono.fromCallable(() -> albumRepository.save(album)));
                }
            });
    }

    public Mono<Album> createAlbumFromJSONSimple(Map<String, Object> albumData) {
        String albumId = (String) albumData.get("id");
        return Mono.fromCallable(() -> albumRepository.findById(albumId))
            .flatMap(repositoryResponse -> {
                if (repositoryResponse.isPresent()) {
                    return Mono.just(repositoryResponse.get());
                } else {
                    return spotifyIntegrationService.getAlbum(albumId);
                }
            });
    }

    public Mono<List<Album>> createAlbumListFromJSON(List<Map<String, Object>> albumsData) {
        return Flux.fromIterable(albumsData)
            .flatMap(this::createAlbumFromJSON)
            .collectList();
    }

    public Mono<List<Album>> createAlbumListFromJSONSimple(List<Map<String, Object>> albumsData) {
        return Flux.fromIterable(albumsData)
            .flatMap(this::createAlbumFromJSONSimple)
            .collectList();
    }

    public Mono<Album> saveAlbum(Album album) {
        return Mono.fromCallable(() -> albumRepository.save(album));
    }
}
