package com.musicrecommender.backend.dto.simplified;

import java.util.List;

import com.musicrecommender.backend.dto.SpotifyImageDTO;

public class SimplifiedAlbumDTO {
    private String albumType;
    private int totalTracks;
    private String href;
    private String id;
    private List<SpotifyImageDTO> images;
    private String name;
    private String releaseDate;
    private String releaseDatePrecision;
    private String uri;
    private List<SimplifiedArtistDTO> artists;

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

    public List<SpotifyImageDTO> getImages() {
        return images;
    }

    public void setImages(List<SpotifyImageDTO> images) {
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

    public List<SimplifiedArtistDTO> getArtists() {
        return artists;
    }

    public void setArtists(List<SimplifiedArtistDTO> artists) {
        this.artists = artists;
    }
}
