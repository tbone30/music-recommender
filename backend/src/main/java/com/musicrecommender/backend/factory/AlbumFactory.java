package com.musicrecommender.backend.factory;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;

import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.entity.SpotifyImage;
import com.musicrecommender.backend.service.ArtistService;
import com.musicrecommender.backend.service.TrackService;

@Component
public class AlbumFactory {
    @Autowired
    @Lazy
    private ArtistService artistService;
    @Autowired
    @Lazy
    private TrackService trackService;

    public Album createAlbumFromJSON(Map<String, Object> albumData) {
        Album album = new Album();
        album.setAlbumType((String) albumData.get("album_type"));
        album.setTotalTracks((Integer) albumData.get("total_tracks"));
        album.setHref((String) albumData.get("href"));
        album.setId((String) albumData.get("id"));
        album.setImages(SpotifyImage.createSpotifyImageListFromJSON((List<Map<String, Object>>) albumData.get("images")));
        album.setName((String) albumData.get("name"));
        album.setReleaseDate((String) albumData.get("release_date"));
        album.setReleaseDatePrecision((String) albumData.get("release_date_precision"));
        album.setUri((String) albumData.get("uri"));
        album.setArtists(artistService.createArtistListFromJSONSimple((List<Map<String, Object>>) albumData.get("artists")));
        album.setTracks(trackService.createTrackListFromJSON((List<Map<String, Object>>) albumData.get("tracks")));
        album.setPopularity((Integer) albumData.get("popularity"));
        return album;
    }
}
