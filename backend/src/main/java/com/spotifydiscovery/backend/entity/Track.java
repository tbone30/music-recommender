package com.spotifydiscovery.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Track Entity for Spotify Discovery Platform
 * 
 * Represents a music track with its metadata and audio features.
 * Stores information retrieved from Spotify Web API and calculated features for ML.
 * 
 * @author Spotify Discovery Team
 */
@Entity
@Table(name = "tracks")
@EntityListeners(AuditingEntityListener.class)
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Spotify ID is required")
    private String spotifyId;

    @Column(nullable = false)
    @NotBlank(message = "Track name is required")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Artist name is required")
    private String artistName;

    @Column
    private String albumName;

    @Column
    private String albumImageUrl;

    @Column
    @Min(value = 0, message = "Duration must be positive")
    private Integer durationMs;

    @Column
    private Integer popularity; // 0-100 from Spotify

    @Column
    private String previewUrl;

    @Column
    private String externalUrl;

    @Column
    private Boolean isExplicit = false;

    // Audio Features (from Spotify Audio Features API)
    @Column
    @DecimalMin(value = "0.0", message = "Acousticness must be between 0.0 and 1.0")
    @DecimalMax(value = "1.0", message = "Acousticness must be between 0.0 and 1.0")
    private Double acousticness;

    @Column
    @DecimalMin(value = "0.0", message = "Danceability must be between 0.0 and 1.0")
    @DecimalMax(value = "1.0", message = "Danceability must be between 0.0 and 1.0")
    private Double danceability;

    @Column
    @DecimalMin(value = "0.0", message = "Energy must be between 0.0 and 1.0")
    @DecimalMax(value = "1.0", message = "Energy must be between 0.0 and 1.0")
    private Double energy;

    @Column
    @DecimalMin(value = "0.0", message = "Instrumentalness must be between 0.0 and 1.0")
    @DecimalMax(value = "1.0", message = "Instrumentalness must be between 0.0 and 1.0")
    private Double instrumentalness;

    @Column
    @DecimalMin(value = "0.0", message = "Liveness must be between 0.0 and 1.0")
    @DecimalMax(value = "1.0", message = "Liveness must be between 0.0 and 1.0")
    private Double liveness;

    @Column
    @DecimalMin(value = "0.0", message = "Loudness must be realistic")
    private Double loudness; // Usually between -60 and 0 db

    @Column
    @DecimalMin(value = "0.0", message = "Speechiness must be between 0.0 and 1.0")
    @DecimalMax(value = "1.0", message = "Speechiness must be between 0.0 and 1.0")
    private Double speechiness;

    @Column
    @DecimalMin(value = "0.0", message = "Valence must be between 0.0 and 1.0")
    @DecimalMax(value = "1.0", message = "Valence must be between 0.0 and 1.0")
    private Double valence;

    @Column
    @DecimalMin(value = "0.0", message = "Tempo must be positive")
    private Double tempo;

    @Column
    private Integer timeSignature; // 3, 4, 5, 6, 7

    @Column
    private Integer key; // 0-11 representing C, C#, D, etc.

    @Column
    private Integer mode; // 0 = minor, 1 = major

    // Genre information
    @Column(length = 1000)
    private String genres; // JSON array of genres

    // Recommendation metadata
    @Column
    private Double recommendationScore;

    @Column
    private Integer playCount = 0;

    @Column
    private Double averageRating;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserTrack> userTracks = new ArrayList<>();

    // Constructors
    public Track() {}

    public Track(String spotifyId, String name, String artistName) {
        this.spotifyId = spotifyId;
        this.name = name;
        this.artistName = artistName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumImageUrl() {
        return albumImageUrl;
    }

    public void setAlbumImageUrl(String albumImageUrl) {
        this.albumImageUrl = albumImageUrl;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public Boolean getIsExplicit() {
        return isExplicit;
    }

    public void setIsExplicit(Boolean isExplicit) {
        this.isExplicit = isExplicit;
    }

    public Double getAcousticness() {
        return acousticness;
    }

    public void setAcousticness(Double acousticness) {
        this.acousticness = acousticness;
    }

    public Double getDanceability() {
        return danceability;
    }

    public void setDanceability(Double danceability) {
        this.danceability = danceability;
    }

    public Double getEnergy() {
        return energy;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
    }

    public Double getInstrumentalness() {
        return instrumentalness;
    }

    public void setInstrumentalness(Double instrumentalness) {
        this.instrumentalness = instrumentalness;
    }

    public Double getLiveness() {
        return liveness;
    }

    public void setLiveness(Double liveness) {
        this.liveness = liveness;
    }

    public Double getLoudness() {
        return loudness;
    }

    public void setLoudness(Double loudness) {
        this.loudness = loudness;
    }

    public Double getSpeechiness() {
        return speechiness;
    }

    public void setSpeechiness(Double speechiness) {
        this.speechiness = speechiness;
    }

    public Double getValence() {
        return valence;
    }

    public void setValence(Double valence) {
        this.valence = valence;
    }

    public Double getTempo() {
        return tempo;
    }

    public void setTempo(Double tempo) {
        this.tempo = tempo;
    }

    public Integer getTimeSignature() {
        return timeSignature;
    }

    public void setTimeSignature(Integer timeSignature) {
        this.timeSignature = timeSignature;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public Double getRecommendationScore() {
        return recommendationScore;
    }

    public void setRecommendationScore(Double recommendationScore) {
        this.recommendationScore = recommendationScore;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<UserTrack> getUserTracks() {
        return userTracks;
    }

    public void setUserTracks(List<UserTrack> userTracks) {
        this.userTracks = userTracks;
    }

    // Utility methods
    public String getDurationFormatted() {
        if (durationMs == null) return "0:00";
        int totalSeconds = durationMs / 1000;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public void incrementPlayCount() {
        this.playCount = (this.playCount == null) ? 1 : this.playCount + 1;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", spotifyId='" + spotifyId + '\'' +
                ", name='" + name + '\'' +
                ", artistName='" + artistName + '\'' +
                ", albumName='" + albumName + '\'' +
                '}';
    }
}
