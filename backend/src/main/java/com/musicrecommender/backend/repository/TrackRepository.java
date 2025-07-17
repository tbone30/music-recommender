package com.musicrecommender.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.musicrecommender.backend.entity.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, String> {
    // Custom query methods can be defined here if needed
}
