package com.musicrecommender.backend.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.musicrecommender.backend.entity.Track;
import com.musicrecommender.backend.service.ArtistService;
import com.musicrecommender.backend.service.AlbumService;

@Component
public class TrackFactory {
    @Autowired
    private AlbumService albumService;
    @Autowired
    private ArtistService artistService;

    public Track createTrackFromJSON(Map<String, Object> trackData) {
        Track track = new Track();
        track.setAlbum(albumService.createAlbumFromJSON((Map<String, Object>) trackData.get("album")));
        track.setArtists(artistService.createArtistListFromJSONSimple((List<Map<String, Object>>) trackData.get("artists")));
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
