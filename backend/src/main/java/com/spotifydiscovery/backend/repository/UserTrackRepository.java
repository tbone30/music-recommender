package com.spotifydiscovery.backend.repository;

import com.spotifydiscovery.backend.entity.UserTrack;
import com.spotifydiscovery.backend.entity.User;
import com.spotifydiscovery.backend.entity.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UserTrack entity operations
 * 
 * Provides CRUD operations and custom queries for user-track interactions,
 * supporting recommendation algorithms and user behavior analysis.
 * 
 * @author Spotify Discovery Team
 */
@Repository
public interface UserTrackRepository extends JpaRepository<UserTrack, Long> {

    /**
     * Find user-track interaction by user and track
     */
    Optional<UserTrack> findByUserAndTrack(User user, Track track);

    /**
     * Find all interactions for a specific user
     */
    List<UserTrack> findByUser(User user);

    /**
     * Find all interactions for a specific user with pagination
     */
    Page<UserTrack> findByUser(User user, Pageable pageable);

    /**
     * Find all interactions for a specific track
     */
    List<UserTrack> findByTrack(Track track);

    /**
     * Find user's favorite tracks
     */
    List<UserTrack> findByUserAndIsFavoriteTrue(User user);

    /**
     * Find user's disliked tracks
     */
    List<UserTrack> findByUserAndIsDislikedTrue(User user);

    /**
     * Find user's highly rated tracks (rating >= 4.0)
     */
    @Query("SELECT ut FROM UserTrack ut WHERE ut.user = :user AND ut.rating >= :minRating ORDER BY ut.rating DESC")
    List<UserTrack> findHighlyRatedTracksByUser(@Param("user") User user, @Param("minRating") Double minRating);

    /**
     * Find user's most played tracks
     */
    @Query("SELECT ut FROM UserTrack ut WHERE ut.user = :user AND ut.playCount > 0 ORDER BY ut.playCount DESC")
    Page<UserTrack> findMostPlayedTracksByUser(@Param("user") User user, Pageable pageable);

    /**
     * Find user's recently played tracks
     */
    @Query("SELECT ut FROM UserTrack ut WHERE ut.user = :user AND ut.lastPlayedAt IS NOT NULL ORDER BY ut.lastPlayedAt DESC")
    Page<UserTrack> findRecentlyPlayedTracksByUser(@Param("user") User user, Pageable pageable);

    /**
     * Find tracks with high engagement scores for a user
     */
    @Query("SELECT ut FROM UserTrack ut WHERE ut.user = :user ORDER BY " +
           "(ut.playCount * 0.3 + COALESCE(ut.rating, 0) * 0.4 + " +
           "CASE WHEN ut.isFavorite = true THEN 20 ELSE 0 END + " +
           "CASE WHEN ut.isDisliked = true THEN -10 ELSE 0 END) DESC")
    List<UserTrack> findTopEngagedTracksByUser(@Param("user") User user, Pageable pageable);

    /**
     * Find tracks played in a specific time range
     */
    @Query("SELECT ut FROM UserTrack ut WHERE ut.user = :user AND ut.lastPlayedAt BETWEEN :startDate AND :endDate")
    List<UserTrack> findTracksByUserInTimeRange(@Param("user") User user, 
                                               @Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);

    /**
     * Find users who liked a specific track (for collaborative filtering)
     */
    @Query("SELECT ut FROM UserTrack ut WHERE ut.track = :track AND (ut.rating >= 4.0 OR ut.isFavorite = true)")
    List<UserTrack> findUsersWhoLikedTrack(@Param("track") Track track);

    /**
     * Find similar users based on shared liked tracks
     */
    @Query("SELECT ut2.user FROM UserTrack ut1 " +
           "JOIN UserTrack ut2 ON ut1.track = ut2.track " +
           "WHERE ut1.user = :user AND ut2.user != :user " +
           "AND ut1.rating >= 4.0 AND ut2.rating >= 4.0 " +
           "GROUP BY ut2.user " +
           "HAVING COUNT(ut1.track) >= :minSharedTracks")
    List<User> findSimilarUsers(@Param("user") User user, @Param("minSharedTracks") Long minSharedTracks);

    /**
     * Find tracks that users with similar taste liked
     */
    @Query("SELECT DISTINCT ut2.track FROM UserTrack ut1 " +
           "JOIN UserTrack ut2 ON ut1.user != ut2.user " +
           "WHERE ut1.user = :user AND ut1.rating >= 4.0 " +
           "AND ut2.rating >= 4.0 AND ut2.track NOT IN " +
           "(SELECT ut3.track FROM UserTrack ut3 WHERE ut3.user = :user)")
    List<Track> findRecommendedTracksFromSimilarUsers(@Param("user") User user, Pageable pageable);

    /**
     * Get user's listening statistics
     */
    @Query("SELECT COUNT(ut), AVG(ut.rating), SUM(ut.playCount) FROM UserTrack ut WHERE ut.user = :user")
    Object[] getUserListeningStats(@Param("user") User user);

    /**
     * Find tracks by context (playlist, device, time)
     */
    @Query("SELECT ut FROM UserTrack ut WHERE ut.user = :user AND " +
           "(:playlistContext IS NULL OR ut.playlistContext = :playlistContext) AND " +
           "(:deviceType IS NULL OR ut.deviceType = :deviceType) AND " +
           "(:timeOfDay IS NULL OR ut.timeOfDay = :timeOfDay)")
    List<UserTrack> findTracksByContext(@Param("user") User user,
                                       @Param("playlistContext") String playlistContext,
                                       @Param("deviceType") String deviceType,
                                       @Param("timeOfDay") String timeOfDay);

    /**
     * Find tracks with high skip rates (for negative feedback)
     */
    @Query("SELECT ut FROM UserTrack ut WHERE ut.user = :user AND ut.skipCount > ut.completedPlays")
    List<UserTrack> findSkippedTracksByUser(@Param("user") User user);

    /**
     * Calculate average rating for a track
     */
    @Query("SELECT AVG(ut.rating) FROM UserTrack ut WHERE ut.track = :track AND ut.rating IS NOT NULL")
    Double calculateAverageRating(@Param("track") Track track);

    /**
     * Count total plays for a track
     */
    @Query("SELECT SUM(ut.playCount) FROM UserTrack ut WHERE ut.track = :track")
    Long getTotalPlaysForTrack(@Param("track") Track track);

    /**
     * Find tracks played by user in specific time periods for habit analysis
     */
    @Query("SELECT ut FROM UserTrack ut WHERE ut.user = :user AND ut.timeOfDay = :timeOfDay")
    List<UserTrack> findTracksByTimeOfDay(@Param("user") User user, @Param("timeOfDay") String timeOfDay);

    /**
     * Check if user has interacted with track
     */
    boolean existsByUserAndTrack(User user, Track track);

    /**
     * Delete old interactions (for data cleanup)
     */
    @Query("DELETE FROM UserTrack ut WHERE ut.lastPlayedAt < :cutoffDate")
    void deleteOldInteractions(@Param("cutoffDate") LocalDateTime cutoffDate);
}
