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
import com.musicrecommender.backend.service.SpotifyIntegrationService;
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
    @Autowired
    private SpotifyIntegrationService spotifyIntegrationService;

    // Create track from JSON data (works with both client credentials and user tokens)
    @SuppressWarnings("unchecked")
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

    /**
     * Overloaded version that accepts already-resolved artist entities, to avoid redundant lookups.
     * @param trackData The track JSON data
     * @param artists The list of Artist entities to associate with the track
     * @return Mono<Track>
     */
    @SuppressWarnings("unchecked")
    public Mono<Track> createTrackFromJSON(Map<String, Object> trackData, List<Artist> artists) {
        Track track = new Track();
        track.setDuration((Integer) trackData.get("duration_ms"));
        track.setExplicit((Boolean) trackData.get("explicit"));
        track.setHref((String) trackData.get("href"));
        track.setId((String) trackData.get("id"));
        track.setName((String) trackData.get("name"));
        track.setUri((String) trackData.get("uri"));
        track.setPopularity((Integer) trackData.get("popularity"));
        track.setAlbumId((String) ((Map<String, Object>) trackData.get("album")).get("id"));

        track.setArtists(artists != null ? artists : List.of());
        return Mono.fromCallable(() -> trackRepository.save(track));
    }
}