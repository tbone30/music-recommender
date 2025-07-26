package com.musicrecommender.backend.entity.simplified;

import java.util.List;

import org.springframework.stereotype.Component;

import com.musicrecommender.backend.entity.SpotifyImage;

@Component
public class SimplifiedAlbum {
    private String albumType;
    private int totalTracks;
    private String href;
    private String id;
    private List<SpotifyImage> images;
    private String name;
    private String releaseDate;
    private String releaseDatePrecision;
    private String uri;
    private List<SimplifiedArtist> artists;

    public String getAlbumType() {
        return albumType;
    }

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }

    public int getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(int totalTracks) {
        this.totalTracks = totalTracks;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SpotifyImage> getImages() {
        return images;
    }

    public void setImages(List<SpotifyImage> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDatePrecision() {
        return releaseDatePrecision;
    }

    public void setReleaseDatePrecision(String releaseDatePrecision) {
        this.releaseDatePrecision = releaseDatePrecision;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<SimplifiedArtist> getArtists() {
        return artists;
    }

    public void setArtists(List<SimplifiedArtist> artists) {
        this.artists = artists;
    }
}
