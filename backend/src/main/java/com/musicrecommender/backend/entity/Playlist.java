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

    
}
