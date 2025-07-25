package com.musicrecommender.backend.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.entity.Playlist;
import com.musicrecommender.backend.entity.SpotifyImage;
import com.musicrecommender.backend.factory.PlaylistFactory;
import com.musicrecommender.backend.repository.PlaylistRepository;
import com.musicrecommender.backend.service.SpotifyIntegrationService;
import com.musicrecommender.backend.dto.PlaylistListPageDTO;
import com.musicrecommender.backend.dto.SpotifyImageDTO;
import com.musicrecommender.backend.dto.simplified.SimplifiedPlaylistDTO;

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
    private static final Logger logger = LoggerFactory.getLogger(PlaylistService.class);

    public Mono<Playlist> getPlaylist(String id) {
        Mono<Playlist> result = spotifyIntegrationService.getPlaylist(id)
                .flatMap(playlistData -> createPlaylistFromJSON(playlistData));
        return result;
    }

    // Overloaded method to get playlist with access token (authenticated user)
    public Mono<Playlist> getPlaylist(String id, String accessToken) {
        Mono<Playlist> result = spotifyIntegrationService.getPlaylist(id, accessToken)
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

        Mono<Map<String, Object>> playlistDataMono;
        if (nextUrl != null) {
            // Has more pages - fetch all tracks
            playlistDataMono = spotifyIntegrationService.fetchAllTracksForPlaylist(tracksMap)
                .map(allTracks -> {
                    // Replace tracks in playlistData with complete set
                    Map<String, Object> completeTracksObject = Map.of(
                        "items", allTracks,
                        "total", allTracks.size()
                    );
                    playlistData.put("tracks", completeTracksObject);
                    return playlistData;
                });
        } else {
            playlistDataMono = Mono.just(playlistData);
        }

        return playlistDataMono
            .flatMap(finalPlaylistData ->
                playlistFactory.createPlaylistFromJSON(finalPlaylistData)
            )
            .flatMap(newPlaylist ->
                Mono.fromCallable(() -> {
                    // Try to find existing playlist
                    var existingOpt = playlistRepository.findById(playlistId);
                    if (existingOpt.isPresent()) {
                        Playlist existing = existingOpt.get();
                        existing.setCollaborative(newPlaylist.getCollaborative());
                        existing.setDescription(newPlaylist.getDescription());
                        existing.setHref(newPlaylist.getHref());
                        existing.setImages(newPlaylist.getImages());
                        existing.setName(newPlaylist.getName());
                        existing.setOwnerDisplayName(newPlaylist.getOwnerDisplayName());
                        existing.getTracks().clear();
                        existing.getTracks().addAll(newPlaylist.getTracks());
                        return playlistRepository.save(existing);
                    } else {
                        return playlistRepository.save(newPlaylist);
                    }
                })
            );
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

    public Mono<PlaylistListPageDTO> getCurrentUserPlaylists(String accessToken) {
        return spotifyIntegrationService.getUserPlaylists(accessToken)
            .map(playlistData -> {
                PlaylistListPageDTO playlistListPageDTO = new PlaylistListPageDTO();
                playlistListPageDTO.setHref((String) playlistData.get("href"));
                playlistListPageDTO.setLimit((Integer) playlistData.get("limit"));
                playlistListPageDTO.setNext((String) playlistData.get("next"));
                playlistListPageDTO.setOffset((Integer) playlistData.get("offset"));
                playlistListPageDTO.setPrevious((String) playlistData.get("previous"));
                playlistListPageDTO.setTotal((Integer) playlistData.get("total"));

                List<Map<String, Object>> items = (List<Map<String, Object>>) playlistData.get("items");
                List<SimplifiedPlaylistDTO> simplifiedPlaylists = items.stream()
                    .map(item -> {
                        SimplifiedPlaylistDTO simplifiedPlaylist = new SimplifiedPlaylistDTO();
                        simplifiedPlaylist.setCollaborative((Boolean) item.get("collaborative"));
                        simplifiedPlaylist.setDescription((String) item.get("description"));
                        simplifiedPlaylist.setHref((String) item.get("href"));
                        simplifiedPlaylist.setId((String) item.get("id"));
                        simplifiedPlaylist.setName((String) item.get("name"));
                        simplifiedPlaylist.setUri((String) item.get("uri"));
                        simplifiedPlaylist.setOwnerId((String) ((Map<String, Object>) item.get("owner")).get("id"));
                        simplifiedPlaylist.setOwnerDisplayName((String) ((Map<String, Object>) item.get("owner")).get("display_name"));

                        List<Map<String, Object>> imagesData = (List<Map<String, Object>>) item.get("images");
                        List<SpotifyImage> images = SpotifyImage.createSpotifyImageListFromJSON(imagesData);
                        List<SpotifyImageDTO> imageDTOs = images.stream().map(image -> new SpotifyImageDTO(image)).toList();
                        simplifiedPlaylist.setImages(imageDTOs);

                        return simplifiedPlaylist;
                    })
                    .toList();
                playlistListPageDTO.setItems(simplifiedPlaylists);

                return playlistListPageDTO;
            });
    }
}
