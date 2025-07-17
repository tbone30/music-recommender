package com.musicrecommender.backend.service;

import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import com.musicrecommender.backend.entity.Track;
import com.musicrecommender.backend.repository.TrackRepository;
import com.musicrecommender.backend.factory.TrackFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TrackService {
    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private TrackFactory trackFactory;

    public Track getTrackById(String id) {
        return trackRepository.findById(id).orElse(null);
    }

    public Track createTrackFromJSON(Map<String, Object> trackData) {
        Optional<Track> repositoryResponse = trackRepository.findById((String) trackData.get("id"));
        if (repositoryResponse.isPresent()) {
            return repositoryResponse.get();
        } else {
            return trackRepository.save(trackFactory.createTrackFromJSON(trackData));
        }
    }

    public Track createTrackFromJSONSimple(Map<String, Object> trackData) {
        Optional<Track> repositoryResponse = trackRepository.findById((String) trackData.get("id"));
        if (repositoryResponse.isPresent()) {
            return repositoryResponse.get();
        } else {
            return createTrackFromJSON(trackData);
        }
    }

    public List<Track> createTrackListFromJSON(List<Map<String, Object>> tracksData) {
        List<Track> tracks = new ArrayList<>();
        for (Map<String, Object> trackData : tracksData) {
            tracks.add(createTrackFromJSON(trackData));
        }
        return tracks;
    }

    public Track saveTrack(Track track) {
        return trackRepository.save(track);
    }
}
