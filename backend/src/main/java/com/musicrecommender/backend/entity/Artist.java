package com.musicrecommender.backend.entity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "artists")
public class Artist {
    @Id
    private String id;
    private String href;
    private String name;
    private int followers;
    private int popularity;
    @ElementCollection
    @CollectionTable(name = "artist_images", joinColumns = @JoinColumn(name = "artist_id"))
    private List<SpotifyImage> images; 
    private String uri;
    private List<String> genres;

    public Artist() {
        // Default constructor for JPA
    }

    public Artist(String id, String href, String name, int followers, int popularity, List<SpotifyImage> images, String uri, List<String> genres) {
        this.id = id;
        this.href = href;
        this.name = name;
        this.followers = followers;
        this.popularity = popularity;
        this.images = images;
        this.uri = uri;
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public String getHref() {
        return href;
    }

    public String getName() {
        return name;
    }

    public int getFollowers() {
        return followers;
    }

    public int getPopularity() {
        return popularity;
    }

    public List<SpotifyImage> getImages() {
        return images;
    }

    public String getUri() {
        return uri;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setImages(List<SpotifyImage> images) {
        this.images = images;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id='" + id + '\'' +
                ", href='" + href + '\'' +
                ", name='" + name + '\'' +
                ", popularity=" + popularity +
                ", uri='" + uri + '\'' +
                ", genres=" + genres +
                '}';
    }
}
