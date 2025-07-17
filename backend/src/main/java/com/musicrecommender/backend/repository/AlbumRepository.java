package com.musicrecommender.backend.repository;

import com.musicrecommender.backend.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, String> {
    // Custom query methods can be defined here if needed
}
