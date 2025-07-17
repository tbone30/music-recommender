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

import reactor.core.publisher.Mono;

@Component
public class AlbumFactory {
    @Autowired
    @Lazy
    private ArtistService artistService;
    @Autowired
    @Lazy
    private TrackService trackService;

    public Mono<Album> createAlbumFromJSON(Map<String, Object> albumData) {
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
        album.setPopularity((Integer) albumData.get("popularity"));
        
        List<Map<String, Object>> artistsData = (List<Map<String, Object>>) albumData.get("artists");
        Mono<List<com.musicrecommender.backend.entity.Artist>> artistsMono = 
            artistsData != null ? artistService.createArtistListFromJSONSimple(artistsData) : Mono.just(List.of());

        List<Map<String, Object>> tracksData = (List<Map<String, Object>>) albumData.get("tracks");
        Mono<List<com.musicrecommender.backend.entity.Track>> tracksMono = 
            tracksData != null ? trackService.createTrackListFromJSON(tracksData) : Mono.just(List.of());

        return Mono.zip(artistsMono, tracksMono)
            .map(tuple -> {
                album.setArtists(tuple.getT1()); // Set artists
                album.setTracks(tuple.getT2());  // Set tracks
                return album;
            });
    }
}
