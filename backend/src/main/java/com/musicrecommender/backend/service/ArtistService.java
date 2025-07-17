package com.musicrecommender.backend.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

import com.musicrecommender.backend.entity.Artist;
import com.musicrecommender.backend.repository.ArtistRepository;
import com.musicrecommender.backend.factory.ArtistFactory;

@Service
public class ArtistService {
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistFactory artistFactory;

    @Autowired
    @Lazy
    private SpotifyIntegrationService spotifyIntegrationService;

    public Artist getArtistById(String id) {
        return artistRepository.findById(id).orElse(null);
    }

    public Artist createArtistFromJSON(Map<String, Object> artistData) {
        Optional<Artist> repositoryResponse = artistRepository.findById((String) artistData.get("id"));
        if (repositoryResponse.isPresent()) {
            return repositoryResponse.get();
        } else {
            return artistRepository.save(artistFactory.createArtistFromJSON(artistData));
        }
    }

    public Artist createArtistFromJSONSimple(Map<String, Object> artistData) {
        Optional<Artist> repositoryResponse = artistRepository.findById((String) artistData.get("id"));
        if (repositoryResponse.isPresent()) {
            return repositoryResponse.get();
        } else {
            return spotifyIntegrationService.getArtist((String) artistData.get("id")).block();
        }
    }

    public List<Artist> createArtistListFromJSON(List<Map<String, Object>> artistsData) {
        List<Artist> artists = new ArrayList<>();
        for (Map<String, Object> artistData : artistsData) {
            artists.add(createArtistFromJSON(artistData));
        }
        return artists;
    }

    public List<Artist> createArtistListFromJSONSimple(List<Map<String, Object>> artistsData) {
        List<Artist> artists = new ArrayList<>();
        for (Map<String, Object> artistData : artistsData) {
            artists.add(createArtistFromJSONSimple(artistData));
        }
        return artists;
    }

    public Artist saveArtist(Artist artist) {
        return artistRepository.save(artist);
    }
}
