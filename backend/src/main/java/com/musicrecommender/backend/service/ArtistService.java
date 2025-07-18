package com.musicrecommender.backend.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

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

    public Mono<Artist> getArtistById(String id) {
        return Mono.fromCallable(() -> artistRepository.findById(id).orElse(null));
    }

    public Mono<Artist> createArtistFromJSON(Map<String, Object> artistData) {
        String artistId = (String) artistData.get("id");
        return Mono.fromCallable(() -> artistRepository.findById(artistId))
            .flatMap(repositoryResponse -> {
                if (repositoryResponse.isPresent()) {
                    return Mono.just(repositoryResponse.get());
                } else {
                    return artistFactory.createArtistFromJSON(artistData)
                        .flatMap(artist -> Mono.fromCallable(() -> artistRepository.save(artist)));
                }
            });
    }

    public Mono<Artist> createArtistFromJSONSimple(Map<String, Object> artistData) {
        String artistId = (String) artistData.get("id");
        return Mono.fromCallable(() -> artistRepository.findById(artistId))
            .flatMap(repositoryResponse -> {
                if (repositoryResponse.isPresent()) {
                    return Mono.just(repositoryResponse.get());
                } else {
                    return spotifyIntegrationService.getArtist(artistId);
                }
            });
    }

    public Mono<List<Artist>> createArtistListFromJSON(List<Map<String, Object>> artistsData) {
        return Flux.fromIterable(artistsData)
            .flatMap(this::createArtistFromJSON)
            .collectList();
    }

    public Mono<List<Artist>> createArtistListFromJSONSimple(List<Map<String, Object>> artistsData) {
        List<String> ids = artistsData.stream()
            .map(artist -> (String) artist.get("id"))
            .toList();
        String idsCommaSeparated = String.join(",", ids);
        return spotifyIntegrationService.getSeveralArtists(idsCommaSeparated);
    }

    public Mono<Artist> saveArtist(Artist artist) {
        return Mono.fromCallable(() -> artistRepository.save(artist));
    }
}
