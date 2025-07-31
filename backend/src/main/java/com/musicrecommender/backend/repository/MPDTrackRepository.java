package com.musicrecommender.backend.repository;

import com.musicrecommender.backend.entity.mpd.MPDTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MPDTrackRepository extends JpaRepository<MPDTrack, String> {

}
