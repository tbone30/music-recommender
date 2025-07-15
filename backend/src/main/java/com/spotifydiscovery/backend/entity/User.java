package com.spotifydiscovery.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User Entity for Spotify Discovery Platform
 * 
 * Represents a user with Spotify integration capabilities.
 * Stores user profile information, preferences, and authentication details.
 * 
 * @author Spotify Discovery Team
 */
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Spotify ID is required")
    private String spotifyId;

    @Column(nullable = false)
    @NotBlank(message = "Display name is required")
    @Size(max = 100, message = "Display name must not exceed 100 characters")
    private String displayName;

    @Column(unique = true, nullable = false)
    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;

    @Column
    private String profileImageUrl;

    @Column
    private String country;

    @Column
    private String spotifyAccessToken;

    @Column
    private String spotifyRefreshToken;

    @Column
    private LocalDateTime tokenExpiryTime;

    @Column
    private String subscriptionType; // free, premium

    @Column
    private Integer followersCount;

    // User preferences for recommendations
    @Column
    private String preferredGenres; // JSON string of genres

    @Column
    private Double acousticnessPreference; // 0.0 to 1.0

    @Column
    private Double danceabilityPreference; // 0.0 to 1.0

    @Column
    private Double energyPreference; // 0.0 to 1.0

    @Column
    private Double valencePreference; // 0.0 to 1.0 (musical positivity)

    @Column
    private Boolean isActive = true;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserTrack> userTracks = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Playlist> playlists = new ArrayList<>();

    // Constructors
    public User() {}

    public User(String spotifyId, String displayName, String email) {
        this.spotifyId = spotifyId;
        this.displayName = displayName;
        this.email = email;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSpotifyAccessToken() {
        return spotifyAccessToken;
    }

    public void setSpotifyAccessToken(String spotifyAccessToken) {
        this.spotifyAccessToken = spotifyAccessToken;
    }

    public String getSpotifyRefreshToken() {
        return spotifyRefreshToken;
    }

    public void setSpotifyRefreshToken(String spotifyRefreshToken) {
        this.spotifyRefreshToken = spotifyRefreshToken;
    }

    public LocalDateTime getTokenExpiryTime() {
        return tokenExpiryTime;
    }

    public void setTokenExpiryTime(LocalDateTime tokenExpiryTime) {
        this.tokenExpiryTime = tokenExpiryTime;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public String getPreferredGenres() {
        return preferredGenres;
    }

    public void setPreferredGenres(String preferredGenres) {
        this.preferredGenres = preferredGenres;
    }

    public Double getAcousticnessPreference() {
        return acousticnessPreference;
    }

    public void setAcousticnessPreference(Double acousticnessPreference) {
        this.acousticnessPreference = acousticnessPreference;
    }

    public Double getDanceabilityPreference() {
        return danceabilityPreference;
    }

    public void setDanceabilityPreference(Double danceabilityPreference) {
        this.danceabilityPreference = danceabilityPreference;
    }

    public Double getEnergyPreference() {
        return energyPreference;
    }

    public void setEnergyPreference(Double energyPreference) {
        this.energyPreference = energyPreference;
    }

    public Double getValencePreference() {
        return valencePreference;
    }

    public void setValencePreference(Double valencePreference) {
        this.valencePreference = valencePreference;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    // Utility methods
    public boolean isTokenExpired() {
        return tokenExpiryTime != null && LocalDateTime.now().isAfter(tokenExpiryTime);
    }

    public void updateTokens(String accessToken, String refreshToken, int expiresInSeconds) {
        this.spotifyAccessToken = accessToken;
        this.spotifyRefreshToken = refreshToken;
        this.tokenExpiryTime = LocalDateTime.now().plusSeconds(expiresInSeconds);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", spotifyId='" + spotifyId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
