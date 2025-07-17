package com.musicrecommender.backend.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.repository.AlbumRepository;
import com.musicrecommender.backend.factory.AlbumFactory;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private AlbumFactory albumFactory;

    @Autowired
    @Lazy
    private SpotifyIntegrationService spotifyIntegrationService;

    public Album getAlbumById(String id) {
        return albumRepository.findById(id).orElse(null);
    }

    public Album createAlbumFromJSON(Map<String, Object> albumData) {
        Optional<Album> repositoryResponse = albumRepository.findById((String) albumData.get("id"));
        if (repositoryResponse.isPresent()) {
            return repositoryResponse.get();
        } else {
            return albumRepository.save(albumFactory.createAlbumFromJSON(albumData));
        }
    }

    public Album createAlbumFromJSONSimple(Map<String, Object> albumData) {
        Optional<Album> repositoryResponse = albumRepository.findById((String) albumData.get("id"));
        if (repositoryResponse.isPresent()) {
            return repositoryResponse.get();
        } else {
            return spotifyIntegrationService.getAlbum((String) albumData.get("id")).block();
        }
    }

    public List<Album> createAlbumListFromJSON(List<Map<String, Object>> albumsData) {
        List<Album> albums = new ArrayList<>();
        for (Map<String, Object> albumData : albumsData) {
            albums.add(createAlbumFromJSON(albumData));
        }
        return albums;
    }

    public List<Album> createAlbumListFromJSONSimple(List<Map<String, Object>> albumsData) {
        List<Album> albums = new ArrayList<>();
        for (Map<String, Object> albumData : albumsData) {
            albums.add(createAlbumFromJSONSimple(albumData));
        }
        return albums;
    }

    public Album saveAlbum(Album album) {
        return albumRepository.save(album);
    }
}
