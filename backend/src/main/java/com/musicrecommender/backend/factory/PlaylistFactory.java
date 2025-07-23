package com.musicrecommender.backend.factory;

import java.util.List;
import java.util.Map;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.musicrecommender.backend.service.TrackService;
import com.musicrecommender.backend.entity.Playlist;
import com.musicrecommender.backend.entity.SpotifyImage;
import com.musicrecommender.backend.entity.Track;

@Component
public class PlaylistFactory {
    @Autowired
    private TrackService trackService;

    public Mono<Playlist> createPlaylistFromJSON(Map<String, Object> playlistData) {
        Playlist playlist = new Playlist();
        playlist.setCollaborative((Boolean) playlistData.get("collaborative"));
        playlist.setDescription((String) playlistData.get("description"));
        playlist.setHref((String) playlistData.get("href"));
        playlist.setId((String) playlistData.get("id"));
        playlist.setImages(SpotifyImage.createSpotifyImageListFromJSON((List<Map<String, Object>>) playlistData.get("images")));
        playlist.setName((String) playlistData.get("name"));
        playlist.setOwnerId((String) ((Map<String, Object>) playlistData.get("owner")).get("id"));
        playlist.setOwnerDisplayName((String) ((Map<String, Object>) playlistData.get("owner")).get("display_name"));
        playlist.setIsPublic((Boolean) playlistData.get("public"));
        playlist.setUri((String) playlistData.get("uri"));

        List<Map<String, Object>> items = (List<Map<String, Object>>) ((Map<String, Object>) playlistData.get("tracks")).get("items");
        List<Map<String, Object>> tracksData = items.stream()
            .map(item -> (Map<String, Object>) item.get("track"))
            .toList();
            
        Mono<List<Track>> tracksMono = 
            tracksData != null ? trackService.createTrackListFromJSON(tracksData) : Mono.just(List.of());

        return tracksMono
            .map(tracks -> {
                playlist.setTracks(tracks);
                return playlist;
            }); 
    }
}
