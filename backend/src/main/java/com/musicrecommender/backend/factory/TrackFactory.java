package com.musicrecommender.backend.factory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.musicrecommender.backend.entity.Track;
import com.musicrecommender.backend.service.ArtistService;

import reactor.core.publisher.Mono;

import com.musicrecommender.backend.service.AlbumService;

@Component
public class TrackFactory {
    @Autowired
    @Lazy
    private AlbumService albumService;
    @Autowired
    @Lazy
    private ArtistService artistService;

    public Mono<Track> createTrackFromJSON(Map<String, Object> trackData) {
        Track track = new Track();
        track.setDuration((Integer) trackData.get("duration_ms"));
        track.setExplicit((Boolean) trackData.get("explicit"));
        track.setHref((String) trackData.get("href"));
        track.setId((String) trackData.get("id"));
        track.setName((String) trackData.get("name"));
        track.setUri((String) trackData.get("uri"));
        track.setPopularity((Integer) trackData.get("popularity"));

        Map<String, Object> albumData = (Map<String, Object>) trackData.get("album");
        Mono<com.musicrecommender.backend.entity.Album> albumMono = 
            albumData != null ? albumService.createAlbumFromJSON(albumData) : Mono.empty();

        List<Map<String, Object>> artistsData = (List<Map<String, Object>>) trackData.get("artists");
        Mono<List<com.musicrecommender.backend.entity.Artist>> artistsMono = 
            artistsData != null ? artistService.createArtistListFromJSONSimple(artistsData) : Mono.just(List.of());

        return Mono.zip(albumMono, artistsMono)
            .map(tuple -> {
                track.setAlbum(tuple.getT1());
                track.setArtists(tuple.getT2());
                return track;
            });
    }
}
