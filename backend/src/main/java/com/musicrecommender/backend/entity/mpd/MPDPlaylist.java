package com.musicrecommender.backend.entity.mpd;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
@Table(name = "mpd_playlists")
public class MPDPlaylist {
    private String name;
    @Id
    private long id;
    @ManyToMany
    private List<MPDTrack> tracks;

    public MPDPlaylist() {
        // Default constructor for JPA
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<MPDTrack> getTracks() {
        return tracks;
    }

    public void setTracks(List<MPDTrack> tracks) {
        this.tracks = tracks;
    }
}
