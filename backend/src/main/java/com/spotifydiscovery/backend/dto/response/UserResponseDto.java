package com.spotifydiscovery.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * User Response DTO for API responses
 * 
 * Contains user information to be sent to the frontend.
 * Excludes sensitive information like tokens.
 * 
 * @author Spotify Discovery Team
 */
public class UserResponseDto {

    private Long id;
    private String spotifyId;
    private String displayName;
    private String email;
    private String profileImageUrl;
    private String country;
    private String subscriptionType;
    private Integer followersCount;
    private String preferredGenres;
    private Double acousticnessPreference;
    private Double danceabilityPreference;
    private Double energyPreference;
    private Double valencePreference;
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    // Constructors
    public UserResponseDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSpotifyId() { return spotifyId; }
    public void setSpotifyId(String spotifyId) { this.spotifyId = spotifyId; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getSubscriptionType() { return subscriptionType; }
    public void setSubscriptionType(String subscriptionType) { this.subscriptionType = subscriptionType; }

    public Integer getFollowersCount() { return followersCount; }
    public void setFollowersCount(Integer followersCount) { this.followersCount = followersCount; }

    public String getPreferredGenres() { return preferredGenres; }
    public void setPreferredGenres(String preferredGenres) { this.preferredGenres = preferredGenres; }

    public Double getAcousticnessPreference() { return acousticnessPreference; }
    public void setAcousticnessPreference(Double acousticnessPreference) { this.acousticnessPreference = acousticnessPreference; }

    public Double getDanceabilityPreference() { return danceabilityPreference; }
    public void setDanceabilityPreference(Double danceabilityPreference) { this.danceabilityPreference = danceabilityPreference; }

    public Double getEnergyPreference() { return energyPreference; }
    public void setEnergyPreference(Double energyPreference) { this.energyPreference = energyPreference; }

    public Double getValencePreference() { return valencePreference; }
    public void setValencePreference(Double valencePreference) { this.valencePreference = valencePreference; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
