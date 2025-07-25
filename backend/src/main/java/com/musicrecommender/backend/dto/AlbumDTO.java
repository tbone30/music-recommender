package com.musicrecommender.backend.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.musicrecommender.backend.entity.Album;

public class AlbumDTO {
    private String id;
    private String name;
    private int totalTracks;
    private List<ArtistDTO> artists;
    private List<TrackDTO> tracks;
    private int popularity;
    private String href;
    private String releaseDate;
    private String releaseDatePrecision;
    private List<SpotifyImageDTO> images;
    private String albumType;
    private String uri;

    // Default constructor
    public AlbumDTO() {}

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getTotalTracks() { return totalTracks; }
    public void setTotalTracks(int totalTracks) { this.totalTracks = totalTracks; }

    public List<ArtistDTO> getArtists() { return artists; }
    public void setArtists(List<ArtistDTO> artists) { this.artists = artists; }

    public List<TrackDTO> getTracks() { return tracks; }
    public void setTracks(List<TrackDTO> tracks) { this.tracks = tracks; }

    public int getPopularity() { return popularity; }
    public void setPopularity(int popularity) { this.popularity = popularity; }

    public String getHref() { return href; }
    public void setHref(String href) { this.href = href; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getReleaseDatePrecision() { return releaseDatePrecision; }
    public void setReleaseDatePrecision(String releaseDatePrecision) { this.releaseDatePrecision = releaseDatePrecision; }

    public List<SpotifyImageDTO> getImages() { return images; }
    public void setImages(List<SpotifyImageDTO> images) { this.images = images; }

    public String getAlbumType() { return albumType; }
    public void setAlbumType(String albumType) { this.albumType = albumType; }

    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }
}