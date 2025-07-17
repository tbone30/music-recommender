package com.musicrecommender.backend.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.musicrecommender.backend.service.SpotifyIntegrationService;
import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.entity.Artist;
import com.musicrecommender.backend.entity.Track;
import com.musicrecommender.backend.repository.TrackRepository;
import com.musicrecommender.backend.factory.ArtistFactory;
import com.musicrecommender.backend.factory.AlbumFactory;

@Component
public class TrackFactory {
    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private SpotifyIntegrationService spotifyIntegrationService;
    @Autowired
    private AlbumFactory albumFactory;
    @Autowired
    private ArtistFactory artistFactory;

    public Track createTrackFromJSON(Map<String, Object> trackData) {
        Track track = new Track();
        track.setAlbum(albumFactory.createAlbumFromJSON((Map<String, Object>) trackData.get("album")));
        track.setArtists(
            ((List<Map<String, Object>>) trackData.get("artists")).stream()
                .map(artistFactory::createArtistFromJSONSimple)
                .toList()
        );
        track.setDuration((Integer) trackData.get("duration_ms"));
        track.setExplicit((Boolean) trackData.get("explicit"));
        track.setHref((String) trackData.get("href"));
        track.setId((String) trackData.get("id"));
        track.setName((String) trackData.get("name"));
        track.setUri((String) trackData.get("uri"));
        track.setPopularity((Integer) trackData.get("popularity"));
        return track;
    }
}
