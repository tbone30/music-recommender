package com.spotifydiscovery.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * UserTrack Entity for Spotify Discovery Platform
 * 
 * Represents the relationship between a User and a Track with interaction data.
 * Stores user ratings, play counts, and preferences for recommendation algorithms.
 * 
 * @author Spotify Discovery Team
 */
@Entity
@Table(name = "user_tracks", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "track_id"}))
@EntityListeners(AuditingEntityListener.class)
public class UserTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    @NotNull(message = "Track is required")
    private Track track;

    // User interaction data
    @Column
    @DecimalMin(value = "1.0", message = "Rating must be between 1.0 and 5.0")
    @DecimalMax(value = "5.0", message = "Rating must be between 1.0 and 5.0")
    private Double rating;

    @Column
    private Integer playCount = 0;

    @Column
    private Boolean isFavorite = false;

    @Column
    private Boolean isDisliked = false;

    @Column
    private Boolean isSkipped = false;

    @Column
    private Integer skipCount = 0;

    @Column
    private Integer completedPlays = 0; // Tracks played to completion

    @Column
    private Double listenTimePercentage; // Percentage of track listened to

    @Column
    private LocalDateTime lastPlayedAt;

    @Column
    private LocalDateTime firstPlayedAt;

    // Contextual information for ML
    @Column
    private String playlistContext; // Which playlist was it played from

    @Column
    private String deviceType; // mobile, desktop, speaker, etc.

    @Column
    private String timeOfDay; // morning, afternoon, evening, night

    @Column
    private String dayOfWeek;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public UserTrack() {}

    public UserTrack(User user, Track track) {
        this.user = user;
        this.track = track;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Boolean getIsDisliked() {
        return isDisliked;
    }

    public void setIsDisliked(Boolean isDisliked) {
        this.isDisliked = isDisliked;
    }

    public Boolean getIsSkipped() {
        return isSkipped;
    }

    public void setIsSkipped(Boolean isSkipped) {
        this.isSkipped = isSkipped;
    }

    public Integer getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(Integer skipCount) {
        this.skipCount = skipCount;
    }

    public Integer getCompletedPlays() {
        return completedPlays;
    }

    public void setCompletedPlays(Integer completedPlays) {
        this.completedPlays = completedPlays;
    }

    public Double getListenTimePercentage() {
        return listenTimePercentage;
    }

    public void setListenTimePercentage(Double listenTimePercentage) {
        this.listenTimePercentage = listenTimePercentage;
    }

    public LocalDateTime getLastPlayedAt() {
        return lastPlayedAt;
    }

    public void setLastPlayedAt(LocalDateTime lastPlayedAt) {
        this.lastPlayedAt = lastPlayedAt;
    }

    public LocalDateTime getFirstPlayedAt() {
        return firstPlayedAt;
    }

    public void setFirstPlayedAt(LocalDateTime firstPlayedAt) {
        this.firstPlayedAt = firstPlayedAt;
    }

    public String getPlaylistContext() {
        return playlistContext;
    }

    public void setPlaylistContext(String playlistContext) {
        this.playlistContext = playlistContext;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    // Utility methods
    public void incrementPlayCount() {
        this.playCount = (this.playCount == null) ? 1 : this.playCount + 1;
        this.lastPlayedAt = LocalDateTime.now();
        
        if (this.firstPlayedAt == null) {
            this.firstPlayedAt = LocalDateTime.now();
        }
    }

    public void incrementSkipCount() {
        this.skipCount = (this.skipCount == null) ? 1 : this.skipCount + 1;
        this.isSkipped = true;
    }

    public void markAsCompleted() {
        this.completedPlays = (this.completedPlays == null) ? 1 : this.completedPlays + 1;
    }

    public double getEngagementScore() {
        if (playCount == null || playCount == 0) return 0.0;
        
        double score = 0.0;
        
        // Base score from play count
        score += Math.min(playCount * 10, 50); // Max 50 points for plays
        
        // Bonus for completion rate
        if (completedPlays != null && playCount > 0) {
            double completionRate = (double) completedPlays / playCount;
            score += completionRate * 20; // Max 20 points for completion
        }
        
        // Bonus for rating
        if (rating != null) {
            score += (rating / 5.0) * 20; // Max 20 points for rating
        }
        
        // Bonus for favorites
        if (Boolean.TRUE.equals(isFavorite)) {
            score += 10;
        }
        
        // Penalty for dislikes
        if (Boolean.TRUE.equals(isDisliked)) {
            score -= 15;
        }
        
        // Penalty for skips
        if (skipCount != null && playCount > 0) {
            double skipRate = (double) skipCount / playCount;
            score -= skipRate * 10;
        }
        
        return Math.max(0, Math.min(100, score)); // Normalize to 0-100
    }

    @Override
    public String toString() {
        return "UserTrack{" +
                "id=" + id +
                ", user=" + (user != null ? user.getDisplayName() : "null") +
                ", track=" + (track != null ? track.getName() : "null") +
                ", rating=" + rating +
                ", playCount=" + playCount +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
