package com.musicrecommender.backend.service;

import java.util.ArrayList;
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

    private Mono<Album> createAlbumFromJSON(Map<String, Object> albumData) {
        String albumId = (String) albumData.get("id");
        
        // Check if tracks need pagination
        Object tracksObject = albumData.get("tracks");
        if (tracksObject instanceof Map) {
            Map<String, Object> tracksMap = (Map<String, Object>) tracksObject;
            String nextUrl = (String) tracksMap.get("next");
            Object itemsObject = tracksMap.get("items");
            int currentTrackCount = 0;
            if (itemsObject instanceof List) {
                currentTrackCount = ((List<?>) itemsObject).size();
            }
            
            if (nextUrl != null) {
                // Has more pages - fetch all tracks
                return spotifyIntegrationService.fetchAllTracksForAlbum(albumId)
                    .map(allTracks -> {
                        // Replace tracks in albumData with complete set
                        Map<String, Object> completeTracksObject = Map.of(
                            "items", allTracks,
                            "total", allTracks.size(),
                            "limit", 50,
                            "offset", 0,
                            "next", null  // No more pages
                        );
                        albumData.put("tracks", completeTracksObject);
                        return albumData;
                    })
                    .flatMap(completeAlbumData -> {
                        return albumFactory.createAlbumFromJSON(completeAlbumData);
                    })
                    .flatMap(album -> {
                        return Mono.fromCallable(() -> albumRepository.save(album));
                    });
            }
        }
        
        // Only one page of tracks or no tracks
        return albumFactory.createAlbumFromJSON(albumData)
            .flatMap(album -> {
                return Mono.fromCallable(() -> albumRepository.save(album));
            });
    }


    public Mono<Album> createAlbumFromJSONSimple(Map<String, Object> albumData) {
        String albumId = (String) albumData.get("id");
        
        return Mono.fromCallable(() -> albumRepository.findById(albumId))
            .flatMap(repositoryResponse -> {
                if (repositoryResponse.isPresent()) {
                    return Mono.just(repositoryResponse.get());
                } else {
                    return spotifyIntegrationService.getAlbum(albumId)
                                                    .flatMap(album -> createAlbumFromJSON(album));
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
