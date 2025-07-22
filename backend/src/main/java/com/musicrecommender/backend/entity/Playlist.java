package com.musicrecommender.backend.entity;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Playlist {
    private Boolean collaborative;
    private String description;
    private String href;
    @Id
    private String id;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "playlist_images", joinColumns = @JoinColumn(name = "playlist_id"))
    private List<SpotifyImage> images; 
    private String name;
    private String ownerId;
    private String ownerDisplayName;
    private String isPublic;
    @OneToMany
    private List<Track> tracks;
    private String uri;

    public Playlist() {
        // Default constructor for JPA
    }

    public Boolean getCollaborative() {
        return collaborative;
    }

    public void setCollaborative(Boolean collaborative) {
        this.collaborative = collaborative;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerDisplayName() {
        return ownerDisplayName;
    }

    public void setOwnerDisplayName(String ownerDisplayName) {
        this.ownerDisplayName = ownerDisplayName;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
