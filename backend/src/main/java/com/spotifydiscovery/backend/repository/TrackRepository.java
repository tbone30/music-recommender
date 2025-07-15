package com.spotifydiscovery.backend.repository;

import com.spotifydiscovery.backend.entity.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Track entity operations
 * 
 * Provides CRUD operations and custom queries for track management,
 * including audio feature analysis and recommendation support.
 * 
 * @author Spotify Discovery Team
 */
@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

    /**
     * Find track by Spotify ID
     */
    Optional<Track> findBySpotifyId(String spotifyId);

    /**
     * Find tracks by artist name
     */
    List<Track> findByArtistNameContainingIgnoreCase(String artistName);

    /**
     * Find tracks by track name
     */
    List<Track> findByNameContainingIgnoreCase(String trackName);

    /**
     * Find tracks by album name
     */
    List<Track> findByAlbumNameContainingIgnoreCase(String albumName);

    /**
     * Find tracks by genre (searches in genres JSON field)
     */
    @Query("SELECT t FROM Track t WHERE t.genres LIKE %:genre%")
    List<Track> findByGenre(@Param("genre") String genre);

    /**
     * Find popular tracks (high popularity score)
     */
    @Query("SELECT t FROM Track t WHERE t.popularity >= :minPopularity ORDER BY t.popularity DESC")
    Page<Track> findPopularTracks(@Param("minPopularity") Integer minPopularity, Pageable pageable);

    /**
     * Find tracks with similar audio features for recommendations
     */
    @Query("SELECT t FROM Track t WHERE " +
           "ABS(t.acousticness - :acousticness) < :threshold AND " +
           "ABS(t.danceability - :danceability) < :threshold AND " +
           "ABS(t.energy - :energy) < :threshold AND " +
           "ABS(t.valence - :valence) < :threshold AND " +
           "t.id != :excludeTrackId")
    List<Track> findSimilarTracks(
        @Param("acousticness") Double acousticness,
        @Param("danceability") Double danceability,
        @Param("energy") Double energy,
        @Param("valence") Double valence,
        @Param("threshold") Double threshold,
        @Param("excludeTrackId") Long excludeTrackId,
        Pageable pageable
    );

    /**
     * Find tracks by tempo range
     */
    @Query("SELECT t FROM Track t WHERE t.tempo BETWEEN :minTempo AND :maxTempo")
    List<Track> findByTempoRange(@Param("minTempo") Double minTempo, @Param("maxTempo") Double maxTempo);

    /**
     * Find tracks by key and mode
     */
    List<Track> findByKeyAndMode(Integer key, Integer mode);

    /**
     * Find highly rated tracks
     */
    @Query("SELECT t FROM Track t WHERE t.averageRating >= :minRating ORDER BY t.averageRating DESC")
    Page<Track> findHighlyRatedTracks(@Param("minRating") Double minRating, Pageable pageable);

    /**
     * Find tracks by duration range
     */
    @Query("SELECT t FROM Track t WHERE t.durationMs BETWEEN :minDuration AND :maxDuration")
    List<Track> findByDurationRange(@Param("minDuration") Integer minDuration, @Param("maxDuration") Integer maxDuration);

    /**
     * Find tracks suitable for workout (high energy, high tempo)
     */
    @Query("SELECT t FROM Track t WHERE t.energy > 0.7 AND t.tempo > 120 AND t.danceability > 0.5")
    List<Track> findWorkoutTracks(Pageable pageable);

    /**
     * Find tracks suitable for relaxation (low energy, high acousticness)
     */
    @Query("SELECT t FROM Track t WHERE t.energy < 0.4 AND t.acousticness > 0.5 AND t.valence > 0.3")
    List<Track> findRelaxationTracks(Pageable pageable);

    /**
     * Find tracks suitable for studying (instrumental, moderate energy)
     */
    @Query("SELECT t FROM Track t WHERE t.instrumentalness > 0.5 AND t.energy BETWEEN 0.3 AND 0.7 AND t.speechiness < 0.1")
    List<Track> findStudyTracks(Pageable pageable);

    /**
     * Find trending tracks (high play count, recently added)
     */
    @Query("SELECT t FROM Track t WHERE t.playCount > :minPlayCount ORDER BY t.createdAt DESC")
    Page<Track> findTrendingTracks(@Param("minPlayCount") Integer minPlayCount, Pageable pageable);

    /**
     * Search tracks by multiple criteria
     */
    @Query("SELECT t FROM Track t WHERE " +
           "(:trackName IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :trackName, '%'))) AND " +
           "(:artistName IS NULL OR LOWER(t.artistName) LIKE LOWER(CONCAT('%', :artistName, '%'))) AND " +
           "(:albumName IS NULL OR LOWER(t.albumName) LIKE LOWER(CONCAT('%', :albumName, '%'))) AND " +
           "(:minPopularity IS NULL OR t.popularity >= :minPopularity)")
    Page<Track> searchTracks(
        @Param("trackName") String trackName,
        @Param("artistName") String artistName,
        @Param("albumName") String albumName,
        @Param("minPopularity") Integer minPopularity,
        Pageable pageable
    );

    /**
     * Find tracks missing audio features
     */
    @Query("SELECT t FROM Track t WHERE t.acousticness IS NULL OR t.danceability IS NULL OR t.energy IS NULL")
    List<Track> findTracksWithoutAudioFeatures();

    /**
     * Get tracks by recommendation score range
     */
    @Query("SELECT t FROM Track t WHERE t.recommendationScore BETWEEN :minScore AND :maxScore ORDER BY t.recommendationScore DESC")
    List<Track> findByRecommendationScoreRange(@Param("minScore") Double minScore, @Param("maxScore") Double maxScore, Pageable pageable);

    /**
     * Count tracks by artist
     */
    @Query("SELECT COUNT(t) FROM Track t WHERE t.artistName = :artistName")
    Long countByArtistName(@Param("artistName") String artistName);

    /**
     * Find random tracks for discovery
     */
    @Query(value = "SELECT * FROM tracks ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Track> findRandomTracks(@Param("limit") Integer limit);

    /**
     * Check if track exists by Spotify ID
     */
    boolean existsBySpotifyId(String spotifyId);
}
