package com.musicrecommender.backend.entity;
import jakarta.persistence.*;
import java.util.List;
import java.util.Map;
import com.musicrecommender.backend.entity.Artist;
import com.musicrecommender.backend.entity.Track;
import com.musicrecommender.backend.entity.SpotifyImage;

@Entity
@Table(name = "albums")
public class Album {
    @Id
    private String id;
    private String name;
    private int totalTracks;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "album_artists",
        joinColumns = @JoinColumn(name = "album_id"),
        inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private List<Artist> artists;
    @OneToMany(mappedBy = "albumId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Track> tracks;
    private int popularity;
    private String href;
    private String releaseDate;
    private String releaseDatePrecision; // "year", "month", "day"
    @ElementCollection
    @CollectionTable(name = "album_images", joinColumns = @JoinColumn(name = "album_id"))
    private List<SpotifyImage> images;
    private String albumType; // "album", "single", "compilation"
    private String uri;

    public Album() {
        // Default constructor for JPA
    }

    public Album(String id, String name, int totalTracks, List<Artist> artists, List<Track> tracks, int popularity, String href,
                 String releaseDate, String releaseDatePrecision, List<SpotifyImage> images, String albumType, String uri) {
        this.name = name;
        this.id = id;
        this.totalTracks = totalTracks;
        this.artists = artists;
        this.tracks = tracks;
        this.popularity = popularity;
        this.href = href;
        this.releaseDate = releaseDate;
        this.releaseDatePrecision = releaseDatePrecision;
        this.images = images;
        this.albumType = albumType;
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalTracks() {
        return totalTracks;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public int getPopularity() {
        return popularity;
    }

    public String getHref() {
        return href;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getReleaseDatePrecision() {
        return releaseDatePrecision;
    }

    public List<SpotifyImage> getImages() {
        return images;
    }

    public String getAlbumType() {
        return albumType;
    }

    public String getUri() {
        return uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTotalTracks(int totalTracks) {
        this.totalTracks = totalTracks;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setReleaseDatePrecision(String releaseDatePrecision) {
        this.releaseDatePrecision = releaseDatePrecision;
    }

    public void setImages(List<SpotifyImage> images) {
        this.images = images;
    }

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id='" + id + '\'' +
                ", totalTracks=" + totalTracks +
                ", artists=" + artists +
                ", tracks=" + tracks +
                ", popularity=" + popularity +
                ", href='" + href + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", releaseDatePrecision='" + releaseDatePrecision + '\'' +
                ", images=" + images +
                ", albumType='" + albumType + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
