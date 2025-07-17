package com.musicrecommender.backend.repository;
import com.musicrecommender.backend.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {
    // Custom query methods can be defined here if needed
}