package com.musicrecommender.backend.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.entity.Playlist;
import com.musicrecommender.backend.factory.PlaylistFactory;
import com.musicrecommender.backend.repository.PlaylistRepository;
import com.musicrecommender.backend.service.SpotifyIntegrationService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private PlaylistFactory playlistFactory;
    @Autowired
    private SpotifyIntegrationService spotifyIntegrationService;

    public Mono<Playlist> getPlaylist(String id) {
        Mono<Playlist> result = spotifyIntegrationService.getPlaylist(id)
                .flatMap(playlistData -> createPlaylistFromJSON(playlistData));
        return result;
    }

    public Mono<Playlist> savePlaylist(Playlist playlist) {
        return Mono.fromCallable(() -> playlistRepository.save(playlist));
    }

    private Mono<Playlist> createPlaylistFromJSON(Map<String, Object> playlistData) {
        String playlistId = (String) playlistData.get("id");

        Object tracksObject = playlistData.get("tracks");
        Map<String, Object> tracksMap = (Map<String, Object>) tracksObject;
        String nextUrl = (String) tracksMap.get("next");
        Object itemsObject = tracksMap.get("items");
        int currentTrackCount = 0;
        if (itemsObject instanceof List) {
            currentTrackCount = ((List<?>) itemsObject).size();
        }
        
        if (nextUrl != null) {
            // Has more pages - fetch all tracks
            return spotifyIntegrationService.fetchAllTracksForPlaylist(tracksMap)
                .map(allTracks -> {
                    // Replace tracks in playlistData with complete set
                    Map<String, Object> completeTracksObject = Map.of(
                        "items", allTracks,
                        "total", allTracks.size()
                    );
                    playlistData.put("tracks", completeTracksObject);
                    return playlistData;
                })
                .flatMap(completePlaylistData -> {
                    return playlistFactory.createPlaylistFromJSON(completePlaylistData);
                })
                .flatMap(playlist -> {
                    return Mono.fromCallable(() -> playlistRepository.save(playlist));
                });
        }
        
        // Only one page of tracks or no tracks
        return playlistFactory.createPlaylistFromJSON(playlistData)
            .flatMap(playlist -> {
                return Mono.fromCallable(() -> playlistRepository.save(playlist));
            });
    }

    public Mono<Playlist> createPlaylistFromJSONSimple(Map<String, Object> playlistData) {
        String playlistId = (String) playlistData.get("id");

        return Mono.fromCallable(() -> playlistRepository.findById(playlistId))
            .flatMap(repositoryResponse -> {
                if (repositoryResponse.isPresent()) {
                    return Mono.just(repositoryResponse.get());
                } else {
                    return spotifyIntegrationService.getPlaylist(playlistId)
                                                    .flatMap(playlist -> createPlaylistFromJSON(playlist));
                }
            });
    }
}
