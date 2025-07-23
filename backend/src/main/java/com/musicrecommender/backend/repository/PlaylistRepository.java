package com.musicrecommender.backend.repository;

import com.musicrecommender.backend.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, String> {
    // Custom query methods can be defined here if needed
}
