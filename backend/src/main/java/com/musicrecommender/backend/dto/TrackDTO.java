package com.musicrecommender.backend.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.musicrecommender.backend.entity.Track;
import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.service.AlbumService;

import reactor.core.publisher.Mono;

public class TrackDTO {
    private String id;
    private String name;
    private int duration;
    private boolean explicit;
    private int popularity;
    private List<ArtistDTO> artists;
    private String href;
    private String uri;
    private String albumId;
    private List<SpotifyImageDTO> albumImages;
    private String albumName;

    public TrackDTO() {}

    public int getDurationMs() {
        return duration;
    }

    public void setDurationMs(int durationMs) {
        this.duration = durationMs;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public List<ArtistDTO> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistDTO> artists) {
        this.artists = artists;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getId() { 
        return id; 
    }

    public void setId(String id) { 
        this.id = id; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public List<SpotifyImageDTO> getAlbumImages() {
        return albumImages;
    }

    public void setAlbumImages(List<SpotifyImageDTO> albumImages) {
        this.albumImages = albumImages;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}