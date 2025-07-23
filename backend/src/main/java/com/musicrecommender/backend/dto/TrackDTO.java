package com.musicrecommender.backend.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.musicrecommender.backend.entity.Track;

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

    public TrackDTO() {}
    
    public TrackDTO(Track track) {
        if (track == null) return;

        this.id = track.getId();
        this.name = track.getName();
        this.href = track.getHref();
        this.uri = track.getUri();
        this.duration = track.getDuration();
        this.explicit = track.isExplicit();
        this.popularity = track.getPopularity();
        this.artists = track.getArtists().stream()
            .map(ArtistDTO::new)
            .collect(Collectors.toList());
        this.albumId = track.getAlbumId();
    }

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
}