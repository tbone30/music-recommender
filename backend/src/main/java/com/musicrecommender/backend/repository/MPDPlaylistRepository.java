package com.musicrecommender.backend.repository;

import com.musicrecommender.backend.entity.mpd.MPDPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MPDPlaylistRepository extends JpaRepository<MPDPlaylist, Long> {

}
