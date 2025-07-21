package com.musicrecommender.backend.entity;
import jakarta.persistence.*;
import java.util.List;
import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.entity.Artist;

@Entity
@Table(name = "tracks")
public class Track {
    @Id
    private String id;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "track_artists",
        joinColumns = @JoinColumn(name = "track_id"),
        inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private List<Artist> artists;
    private String albumId;
    private String uri;
    private int duration; // Duration in milliseconds
    private boolean explicit;
    private String href;
    private int popularity; 

    public Track() {
        // Default constructor for JPA
    }

    public Track(String id, String name, List<Artist> artists, String albumId, String uri, int duration, boolean explicit, String href, int popularity) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.albumId = albumId;
        this.uri = uri;
        this.duration = duration;
        this.explicit = explicit;
        this.href = href;
        this.popularity = popularity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getUri() {
        return uri;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public String getHref() {
        return href;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artists=" + artists +
                ", albumId=" + albumId +
                ", uri='" + uri + '\'' +
                ", duration=" + duration +
                ", explicit=" + explicit +
                ", href='" + href + '\'' +
                ", popularity=" + popularity +
                '}';
    }
}
