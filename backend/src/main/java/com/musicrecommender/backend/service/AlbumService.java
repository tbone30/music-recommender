package com.musicrecommender.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.repository.AlbumRepository;
import com.musicrecommender.backend.factory.AlbumFactory;

@Service
public class AlbumService {
    private static final Logger logger = LoggerFactory.getLogger(AlbumService.class);
    
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
        logger.info("AlbumService.createAlbumFromJSON - Processing album ID: {}", albumId);
        
        // Log if album data contains simplified information
        Object tracksObject = albumData.get("tracks");
        if (tracksObject instanceof Map) {
            Map<String, Object> tracksMap = (Map<String, Object>) tracksObject;
            Object itemsObject = tracksMap.get("items");
            int trackCount = 0;
            if (itemsObject instanceof List) {
                trackCount = ((List<?>) itemsObject).size();
            }
            String nextUrl = (String) tracksMap.get("next");
            logger.info("AlbumService.createAlbumFromJSON - Album {} tracks info: count={}, hasNext={}", 
                       albumId, trackCount, nextUrl != null);
        } else {
            logger.info("AlbumService.createAlbumFromJSON - Album {} has no tracks object or not a Map", albumId);
        }
        
        return Mono.fromCallable(() -> albumRepository.findById(albumId))
            .flatMap(repositoryResponse -> {
                if (repositoryResponse.isPresent()) {
                    logger.info("AlbumService.createAlbumFromJSON - Album {} found in repository, returning existing", albumId);
                    return Mono.just(repositoryResponse.get());
                } else {
                    logger.info("AlbumService.createAlbumFromJSON - Album {} not in repository, creating with all tracks", albumId);
                    return createAlbumWithAllTracks(albumData);
                }
            });
    }

    public Mono<Album> createAlbumFromJSONSimple(Map<String, Object> albumData) {
        String albumId = (String) albumData.get("id");
        logger.info("AlbumService.createAlbumFromJSONSimple - Processing album ID: {}", albumId);
        
        // Log if album data contains simplified information
        Object tracksObject = albumData.get("tracks");
        if (tracksObject instanceof Map) {
            Map<String, Object> tracksMap = (Map<String, Object>) tracksObject;
            Object itemsObject = tracksMap.get("items");
            int trackCount = 0;
            if (itemsObject instanceof List) {
                trackCount = ((List<?>) itemsObject).size();
            }
            String nextUrl = (String) tracksMap.get("next");
            logger.info("AlbumService.createAlbumFromJSONSimple - Album {} tracks info: count={}, hasNext={}", 
                       albumId, trackCount, nextUrl != null);
        } else {
            logger.info("AlbumService.createAlbumFromJSONSimple - Album {} has no tracks object or not a Map", albumId);
        }
        
        return Mono.fromCallable(() -> albumRepository.findById(albumId))
            .flatMap(repositoryResponse -> {
                if (repositoryResponse.isPresent()) {
                    logger.info("AlbumService.createAlbumFromJSONSimple - Album {} found in repository, returning existing", albumId);
                    return Mono.just(repositoryResponse.get());
                } else {
                    logger.info("AlbumService.createAlbumFromJSONSimple - Album {} not in repository, fetching from Spotify", albumId);
                    return spotifyIntegrationService.getAlbum(albumId);
                }
            });
    }

    public Mono<List<Album>> createAlbumListFromJSON(List<Map<String, Object>> albumsData) {
        logger.info("AlbumService.createAlbumListFromJSON - Processing {} albums", albumsData.size());
        return Flux.fromIterable(albumsData)
            .flatMap(this::createAlbumFromJSON)
            .collectList()
            .doOnSuccess(albums -> logger.info("AlbumService.createAlbumListFromJSON - Successfully created {} albums", albums.size()));
    }

    public Mono<List<Album>> createAlbumListFromJSONSimple(List<Map<String, Object>> albumsData) {
        logger.info("AlbumService.createAlbumListFromJSONSimple - Processing {} albums", albumsData.size());
        return Flux.fromIterable(albumsData)
            .flatMap(this::createAlbumFromJSONSimple)
            .collectList()
            .doOnSuccess(albums -> logger.info("AlbumService.createAlbumListFromJSONSimple - Successfully created {} albums", albums.size()));
    }

    private Mono<Album> createAlbumWithAllTracks(Map<String, Object> albumData) {
        String albumId = (String) albumData.get("id");
        logger.info("AlbumService.createAlbumWithAllTracks - Starting creation for album ID: {}", albumId);
        
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
            
            logger.info("AlbumService.createAlbumWithAllTracks - Album {} initial tracks: count={}, nextUrl={}", 
                       albumId, currentTrackCount, nextUrl != null ? "present" : "null");
            
            if (nextUrl != null) {
                logger.info("AlbumService.createAlbumWithAllTracks - Album {} has pagination, fetching all tracks", albumId);
                // Has more pages - fetch all tracks
                return spotifyIntegrationService.fetchAllTracksForAlbum(albumId)
                    .map(allTracks -> {
                        logger.info("AlbumService.createAlbumWithAllTracks - Album {} fetched all tracks: count={}", 
                                   albumId, allTracks.size());
                        // Replace tracks in albumData with complete set
                        Map<String, Object> completeTracksObject = Map.of(
                            "items", allTracks,
                            "total", allTracks.size(),
                            "limit", 50,
                            "offset", 0,
                            "next", null  // No more pages
                        );
                        albumData.put("tracks", completeTracksObject);
                        logger.info("AlbumService.createAlbumWithAllTracks - Album {} tracks object updated with complete data", albumId);
                        return albumData;
                    })
                    .flatMap(completeAlbumData -> {
                        logger.info("AlbumService.createAlbumWithAllTracks - Album {} creating album entity from complete data", albumId);
                        return albumFactory.createAlbumFromJSON(completeAlbumData);
                    })
                    .flatMap(album -> {
                        logger.info("AlbumService.createAlbumWithAllTracks - Album {} saving to repository", albumId);
                        return Mono.fromCallable(() -> albumRepository.save(album));
                    });
            }
        }
        
        // Only one page of tracks or no tracks
        logger.info("AlbumService.createAlbumWithAllTracks - Album {} has single page or no tracks, creating directly", albumId);
        return albumFactory.createAlbumFromJSON(albumData)
            .flatMap(album -> {
                logger.info("AlbumService.createAlbumWithAllTracks - Album {} saving single-page album to repository", albumId);
                return Mono.fromCallable(() -> albumRepository.save(album));
            });
    }

    public Mono<Album> saveAlbum(Album album) {
        logger.info("AlbumService.saveAlbum - Saving album ID: {} with {} tracks", 
                   album.getId(), album.getTracks() != null ? album.getTracks().size() : 0);
        return Mono.fromCallable(() -> albumRepository.save(album))
            .doOnSuccess(savedAlbum -> logger.info("AlbumService.saveAlbum - Successfully saved album ID: {}", savedAlbum.getId()));
    }
}
