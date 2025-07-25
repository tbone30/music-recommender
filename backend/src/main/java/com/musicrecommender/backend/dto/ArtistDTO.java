package com.musicrecommender.backend.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.musicrecommender.backend.entity.Artist;

public class ArtistDTO {
    private String id;
    private String name;
    private int popularity;
    private List<String> genres;
    private List<SpotifyImageDTO> images;
    private String href;
    private String uri;

    // Constructors, getters, and setters
    public ArtistDTO() {}

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPopularity() { return popularity; }
    public void setPopularity(int popularity) { this.popularity = popularity; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }

    public List<SpotifyImageDTO> getImages() { return images; }
    public void setImages(List<SpotifyImageDTO> images) { this.images = images; }

    public String getHref() { return href; }
    public void setHref(String href) { this.href = href; }

    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }
}