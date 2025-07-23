// filepath: c:\Users\twelc\Documents\Github\music-recommender\backend\src\main\java\com\musicrecommender\backend\service\AlbumMapperService.java
package com.musicrecommender.backend.service;

import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.entity.Artist;
import com.musicrecommender.backend.entity.Track;
import com.musicrecommender.backend.entity.SpotifyImage;
import com.musicrecommender.backend.dto.*;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DTOMapperService {
    public AlbumDTO albumToDTO(Album album) {
        if (album == null) return null;

        AlbumDTO dto = new AlbumDTO();
        dto.setId(album.getId());
        dto.setName(album.getName());
        dto.setTotalTracks(album.getTotalTracks());
        dto.setPopularity(album.getPopularity());
        dto.setHref(album.getHref());
        dto.setReleaseDate(album.getReleaseDate());
        dto.setReleaseDatePrecision(album.getReleaseDatePrecision());
        dto.setAlbumType(album.getAlbumType());
        dto.setUri(album.getUri());

        dto.setArtists(album.getArtists().stream()
                .map(this::artistToDTO)
                .collect(Collectors.toList()));

        if (album.getTracks() != null) {
            dto.setTracks(album.getTracks().stream()
                    .map(this::trackToDTO)
                    .collect(Collectors.toList()));
        }

        if (album.getImages() != null) {
            dto.setImages(album.getImages().stream()
                    .map(this::imageToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public ArtistDTO artistToDTO(Artist artist) {
        if (artist == null) return null;

        ArtistDTO dto = new ArtistDTO();
        dto.setId(artist.getId());
        dto.setName(artist.getName());
        dto.setPopularity(artist.getPopularity());
        dto.setHref(artist.getHref());
        dto.setUri(artist.getUri());
        dto.setGenres(artist.getGenres());
        dto.setImages(artist.getImages().stream()
                .map(this::imageToDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    public TrackDTO trackToDTO(Track track) {
        if (track == null) return null;

        TrackDTO dto = new TrackDTO();
        dto.setId(track.getId());
        dto.setName(track.getName());
        dto.setHref(track.getHref());
        dto.setUri(track.getUri());
        dto.setDurationMs(track.getDuration());
        dto.setExplicit(track.isExplicit());
        dto.setPopularity(track.getPopularity());
        dto.setArtists(track.getArtists().stream()
            .map(this::artistToDTO)
            .collect(Collectors.toList()));
        dto.setAlbumId(track.getAlbumId());
        return dto;
    }

    public SpotifyImageDTO imageToDTO(SpotifyImage image) {
        if (image == null) return null;

        return new SpotifyImageDTO(
                image.getUrl(),
                image.getHeight(),
                image.getWidth()
        );
    }
}