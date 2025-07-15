package com.spotifydiscovery.backend.repository;

import com.spotifydiscovery.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations
 * 
 * Provides CRUD operations and custom queries for user management,
 * including Spotify integration and user preferences.
 * 
 * @author Spotify Discovery Team
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by Spotify ID
     */
    Optional<User> findBySpotifyId(String spotifyId);

    /**
     * Find user by email address
     */
    Optional<User> findByEmail(String email);

    /**
     * Find all active users
     */
    List<User> findByIsActiveTrue();

    /**
     * Find users by subscription type
     */
    List<User> findBySubscriptionType(String subscriptionType);

    /**
     * Find users with expired tokens that need refresh
     */
    @Query("SELECT u FROM User u WHERE u.tokenExpiryTime < :currentTime AND u.spotifyRefreshToken IS NOT NULL")
    List<User> findUsersWithExpiredTokens(@Param("currentTime") LocalDateTime currentTime);

    /**
     * Find users by country
     */
    List<User> findByCountry(String country);

    /**
     * Find users created after a specific date
     */
    List<User> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find users with specific genre preferences
     */
    @Query("SELECT u FROM User u WHERE u.preferredGenres LIKE %:genre%")
    List<User> findByGenrePreference(@Param("genre") String genre);

    /**
     * Find users with similar music preferences for recommendation
     */
    @Query("SELECT u FROM User u WHERE " +
           "ABS(u.acousticnessPreference - :acousticness) < 0.2 AND " +
           "ABS(u.danceabilityPreference - :danceability) < 0.2 AND " +
           "ABS(u.energyPreference - :energy) < 0.2 AND " +
           "ABS(u.valencePreference - :valence) < 0.2 AND " +
           "u.id != :excludeUserId")
    List<User> findUsersWithSimilarPreferences(
        @Param("acousticness") Double acousticness,
        @Param("danceability") Double danceability,
        @Param("energy") Double energy,
        @Param("valence") Double valence,
        @Param("excludeUserId") Long excludeUserId
    );

    /**
     * Count active users
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    Long countActiveUsers();

    /**
     * Find users who haven't updated their tokens recently
     */
    @Query("SELECT u FROM User u WHERE u.updatedAt < :threshold AND u.isActive = true")
    List<User> findInactiveUsers(@Param("threshold") LocalDateTime threshold);

    /**
     * Check if user exists by Spotify ID
     */
    boolean existsBySpotifyId(String spotifyId);

    /**
     * Check if user exists by email
     */
    boolean existsByEmail(String email);

    /**
     * Find top users by follower count
     */
    List<User> findTop10ByOrderByFollowersCountDesc();

    /**
     * Update user's last activity timestamp
     */
    @Query("UPDATE User u SET u.updatedAt = :currentTime WHERE u.id = :userId")
    void updateLastActivity(@Param("userId") Long userId, @Param("currentTime") LocalDateTime currentTime);
}
