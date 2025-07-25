package com.musicrecommender.backend.service;

import java.util.Map;
import java.util.List;

import com.musicrecommender.backend.entity.Track;
import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.entity.Artist;
import com.musicrecommender.backend.repository.TrackRepository;
import com.musicrecommender.backend.factory.TrackFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

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

    public Mono<Track> getTrack(String id) {
        return Mono.fromCallable(() -> trackRepository.findById(id).orElse(null))
            .flatMap(track -> {
                if (track != null) {
                    return Mono.just(track);
                } else {
                    return spotifyIntegrationService.getTrack(id)
                        .flatMap(trackData -> createTrackFromJSON(trackData));
                }
            });
    }

    public Mono<List<Track>> getSeveralTracks(String ids) {
        // Split ids into batches of 50
        String[] idArray = ids.split(",");
        int batchSize = 50;
        List<List<String>> batches = new java.util.ArrayList<>();
        for (int i = 0; i < idArray.length; i += batchSize) {
            List<String> batch = new java.util.ArrayList<>();
            for (int j = i; j < Math.min(i + batchSize, idArray.length); j++) {
                batch.add(idArray[j]);
            }
            batches.add(batch);
        }

        return Flux.fromIterable(batches)
            .flatMap(batch -> {
                String joinedIds = String.join(",", batch);
                return spotifyIntegrationService.getSeveralTracks(joinedIds);
            })
            .collectList()
            .map(listOfLists -> listOfLists.stream().flatMap(List::stream).toList())
            .flatMap(tracks -> createTrackListFromJSON(tracks));
    }

    public Mono<Track> saveTrack(Track track) {
        return Mono.fromCallable(() -> trackRepository.save(track));
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

    /**
     * Overloaded version that accepts already-resolved artist entities, to avoid redundant lookups.
     * @param trackData The track JSON data
     * @param artists The list of Artist entities to associate with the track
     * @return Mono<Track>
     */
    public Mono<Track> createTrackFromJSON(Map<String, Object> trackData, List<Artist> artists) {
        String trackId = (String) trackData.get("id");
        return Mono.fromCallable(() -> trackRepository.findById(trackId))
            .flatMap(repositoryResponse -> {
                if (repositoryResponse.isPresent()) {
                    return Mono.just(repositoryResponse.get());
                } else {
                    return trackFactory.createTrackFromJSON(trackData, artists)
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
                    return spotifyIntegrationService.getTrack(trackId)
                                                    .flatMap(track -> createTrackFromJSON(track));
                }
            });
    }

    public Mono<List<Track>> createTrackListFromJSON(List<Map<String, Object>> tracksData) {
        return Flux.fromIterable(tracksData)
            .flatMap(this::createTrackFromJSON)
            .collectList();
    }

    public Mono<List<Track>> createTrackListFromJSONSimple(List<Map<String, Object>> tracksData) {
        List<String> ids = tracksData.stream()
            .map(track -> (String) track.get("id"))
            .toList();
        String idsCommaSeparated = String.join(",", ids);
        return getSeveralTracks(idsCommaSeparated);
    }

    /**
     * Overloaded version that accepts a list of artists for each track, to avoid redundant lookups.
     * @param tracksData List of track JSON objects
     * @param artistsList List of artist lists, one per track
     * @return Mono<List<Track>>
     */
    public Mono<List<Track>> createTrackListFromJSONSimple(List<Map<String, Object>> tracksData, List<List<Artist>> artistsList) {
        if (tracksData.size() != artistsList.size()) {
            return Mono.error(new IllegalArgumentException("tracksData and artistsList must be the same size"));
        }
        // Collect all track IDs
        List<String> ids = tracksData.stream()
            .map(track -> (String) track.get("id"))
            .toList();
        String idsCommaSeparated = String.join(",", ids);
        // Batch fetch full track data
        return spotifyIntegrationService.getSeveralTracks(idsCommaSeparated)
            .flatMap(fullTracksData -> {
                if (fullTracksData.size() != artistsList.size()) {
                    return Mono.error(new IllegalStateException("Fetched track data size does not match artists list size"));
                }
                return Flux.range(0, fullTracksData.size())
                    .flatMap(i -> this.createTrackFromJSON(fullTracksData.get(i), artistsList.get(i)))
                    .collectList();
            });
    }
}
