package com.musicrecommender.backend.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.musicrecommender.backend.service.SpotifyIntegrationService;
import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.entity.Artist;
import com.musicrecommender.backend.entity.SpotifyImage;
import com.musicrecommender.backend.repository.AlbumRepository;
import com.musicrecommender.backend.service.ArtistService;
import com.musicrecommender.backend.service.SpotifyImageService;
import com.musicrecommender.backend.service.TrackService;

import java.util.Optional;

@Component
public class AlbumFactory {
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private ArtistService artistService;
    @Autowired
    private SpotifyImageService spotifyImageService;
    @Autowired
    private TrackService trackService;
    @Autowired
    private SpotifyIntegrationService spotifyIntegrationService;

    public Album createAlbumFromJSON(Map<String, Object> albumData) {
        Optional<Album> repositoryResponse = albumRepository.findById((String) albumData.get("id"));
        if (repositoryResponse.isPresent()) {
            return repositoryResponse.get();
        } else {
            Album album = new Album();
            album.setAlbumType((String) albumData.get("album_type"));
            album.setTotalTracks((Integer) albumData.get("total_tracks"));
            album.setHref((String) albumData.get("href"));
            album.setId((String) albumData.get("id"));
            album.setImages(spotifyImageService.createSpotifyImageListFromJSON((List<Map<String, Object>>) albumData.get("images")));
            album.setName((String) albumData.get("name"));
            album.setReleaseDate((String) albumData.get("release_date"));
            album.setReleaseDatePrecision((String) albumData.get("release_date_precision"));
            album.setUri((String) albumData.get("uri"));
            album.setArtists(artistService.createArtistListFromJSONSimple((List<Map<String, Object>>) albumData.get("artists")));
            album.setTracks(trackService.createTrackListFromJSON((List<Map<String, Object>>) albumData.get("tracks")));
            album.setPopularity((Integer) albumData.get("popularity"));
            return albumRepository.save(album);
        }
    }

    public Album createAlbumFromJSONSimple(Map<String, Object> albumData) {
        Optional<Album> repositoryResponse = albumRepository.findById((String) albumData.get("id"));
        if (repositoryResponse.isPresent()) {
            return repositoryResponse.get();
        } else {
            return spotifyIntegrationService.getAlbum((String) albumData.get("id")).block();
        }
    }
}
