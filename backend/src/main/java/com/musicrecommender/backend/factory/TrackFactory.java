package com.musicrecommender.backend.factory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.musicrecommender.backend.entity.Track;
import com.musicrecommender.backend.entity.Artist;
import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.service.ArtistService;
import com.musicrecommender.backend.repository.TrackRepository;

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
    @Autowired
    private TrackRepository trackRepository;

    public Mono<Track> createTrackFromJSON(Map<String, Object> trackData) {
        // Create track object
        Track track = new Track();
        track.setDuration((Integer) trackData.get("duration_ms"));
        track.setExplicit((Boolean) trackData.get("explicit"));
        track.setHref((String) trackData.get("href"));
        track.setId((String) trackData.get("id"));
        track.setName((String) trackData.get("name"));
        track.setUri((String) trackData.get("uri"));
        track.setPopularity((Integer) trackData.get("popularity"));
        track.setAlbumId((String) ((Map<String, Object>) trackData.get("album")).get("id"));

        // Get data for relationships
        List<Map<String, Object>> artistsData = (List<Map<String, Object>>) trackData.get("artists");

        // Load artists first
        Mono<List<Artist>> artistsMono = artistsData != null ? 
            artistService.createArtistListFromJSONSimple(artistsData) : 
            Mono.just(List.<Artist>of());

        return artistsMono
            .flatMap(artists -> {
                track.setArtists(artists);
                return Mono.fromCallable(() -> trackRepository.save(track));
            });
    }
}