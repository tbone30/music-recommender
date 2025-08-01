package com.musicrecommender.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.entity.Track;
import com.musicrecommender.backend.entity.simplified.SimplifiedAlbum;
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
    private static final Logger logger = LoggerFactory.getLogger(AlbumService.class);

    public Mono<Album> getAlbum(String id) {
        logger.info("Fetching album with ID: {}", id);
        logger.info("Album not found in repository, fetching from Spotify");
        Mono<Album> result = spotifyIntegrationService.getAlbum(id)
                .flatMap(albumData -> createAlbumFromJSON(albumData));
        logger.info("Result: {}", result);
        return result;
    }

    public Mono<List<Album>> getSeveralAlbums(String ids) {
        // Split ids into batches of 20
        String[] idArray = ids.split(",");
        int batchSize = 20;
        List<List<String>> batches = new ArrayList<>();
        for (int i = 0; i < idArray.length; i += batchSize) {
            List<String> batch = new ArrayList<>();
            for (int j = i; j < Math.min(i + batchSize, idArray.length); j++) {
                batch.add(idArray[j]);
            }
            batches.add(batch);
        }

        return Flux.fromIterable(batches)
            .flatMap(batch -> {
                String joinedIds = String.join(",", batch);
                return spotifyIntegrationService.getSeveralAlbums(joinedIds);
            })
            .collectList()
            .map(listOfLists -> listOfLists.stream().flatMap(List::stream).toList())
            .flatMap(albums -> createAlbumListFromJSON(albums));
    }

    public Mono<Album> saveAlbum(Album album) {
        return Mono.fromCallable(() -> albumRepository.save(album));
    }

    private Mono<Album> createAlbumFromJSON(Map<String, Object> albumData) {
        logger.info("Creating album from JSON data: {}", albumData);
        String albumId = (String) albumData.get("id");
        
        Object tracksObject = albumData.get("tracks");
        Map<String, Object> tracksMap = (Map<String, Object>) tracksObject;
        String nextUrl = (String) tracksMap.get("next");
        Object itemsObject = tracksMap.get("items");
        int currentTrackCount = 0;
        if (itemsObject instanceof List) {
            currentTrackCount = ((List<?>) itemsObject).size();
        }
        
        if (nextUrl != null) {
            // Has more pages - fetch all tracks
            return spotifyIntegrationService.fetchAllTracksForAlbum(tracksMap)
                .map(allTracks -> {
                    // Replace tracks in albumData with complete set
                    Map<String, Object> completeTracksObject = Map.of(
                        "items", allTracks,
                        "total", allTracks.size()
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
        List<String> ids = albumsData.stream()
            .map(album -> (String) album.get("id"))
            .toList();
        String idsCommaSeparated = String.join(",", ids);
        return getSeveralAlbums(idsCommaSeparated);
    }

    public Mono<SimplifiedAlbum> createSimplifiedAlbumFromJSON(Map<String, Object> albumData) {
        return albumFactory.createSimplifiedAlbumFromJSON(albumData);
    }

    public Mono<List<SimplifiedAlbum>> createSimplifiedAlbumListFromJSON(List<Map<String, Object>> albumsData) {
        return Flux.fromIterable(albumsData)
            .flatMap(this::createSimplifiedAlbumFromJSON)
            .collectList();
    }
}
