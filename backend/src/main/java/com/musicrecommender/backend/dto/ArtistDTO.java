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

    public ArtistDTO(String id, String name, int popularity, List<String> genres, 
                    List<SpotifyImageDTO> images, String href, String uri) {
        this.id = id;
        this.name = name;
        this.popularity = popularity;
        this.genres = genres;
        this.images = images;
        this.href = href;
        this.uri = uri;
    }

    public ArtistDTO(Artist artist) {
        if (artist == null) return;
        this.id = artist.getId();
        this.name = artist.getName();
        this.popularity = artist.getPopularity();
        this.href = artist.getHref();
        this.uri = artist.getUri();
        this.genres = artist.getGenres();
        if (artist.getImages() != null) {
            this.images = artist.getImages().stream()
                .map(SpotifyImageDTO::new)
                .collect(Collectors.toList());
        }
    }

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