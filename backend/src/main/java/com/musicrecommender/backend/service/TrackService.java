package com.musicrecommender.backend.service;

import java.util.Map;
import java.util.List;

import com.musicrecommender.backend.entity.Track;
import com.musicrecommender.backend.repository.TrackRepository;
import com.musicrecommender.backend.factory.TrackFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;


@Service
public class TrackService {
    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private TrackFactory trackFactory;

    @Autowired
    @Lazy
    private SpotifyIntegrationService spotifyIntegrationService;

    public Mono<Track> getTrackById(String id) {
        return Mono.fromCallable(() -> trackRepository.findById(id).orElse(null));
    }

    public Mono<Track> createTrackFromJSON(Map<String, Object> trackData) {
        String trackId = (String) trackData.get("id");
        return Mono.fromCallable(() -> trackRepository.findById(trackId))
            .flatMap(repositoryResponse -> {
                if (repositoryResponse.isPresent()) {
                    return Mono.just(repositoryResponse.get());
                } else {
                    return trackFactory.createTrackFromJSON(trackData)
                        .flatMap(track -> Mono.fromCallable(() -> trackRepository.save(track)));
                }
            });
    }

    public Mono<Track> createTrackFromJSONSimple(Map<String, Object> trackData) {
        String trackId = (String) trackData.get("id");
        return Mono.fromCallable(() -> trackRepository.findById(trackId))
            .flatMap(repositoryResponse -> {
                if (repositoryResponse.isPresent()) {
                    return Mono.just(repositoryResponse.get());
                } else {
                    return spotifyIntegrationService.getTrack(trackId);
                }
            });
    }

    public Mono<List<Track>> createTrackListFromJSON(List<Map<String, Object>> tracksData) {
        return Flux.fromIterable(tracksData)
            .flatMap(this::createTrackFromJSON)
            .collectList();
    }

    public Mono<Track> saveTrack(Track track) {
        return Mono.fromCallable(() -> trackRepository.save(track));
    }
}
