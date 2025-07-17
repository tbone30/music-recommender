package com.musicrecommender.backend.factory;

import java.lang.foreign.Linker.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.musicrecommender.backend.entity.Artist;
import com.musicrecommender.backend.entity.SpotifyImage;
import com.musicrecommender.backend.service.SpotifyImageService;
import com.musicrecommender.backend.repository.ArtistRepository;
import com.musicrecommender.backend.service.SpotifyIntegrationService;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class ArtistFactory {
    @Autowired
    private final ArtistRepository artistRepository;
    @Autowired
    private final SpotifyImageService spotifyImageService;
    @Autowired
    private final SpotifyIntegrationService spotifyIntegrationService;

    public Artist createArtistFromJSON(Map<String, Object> artistData) {
        Optional<Artist> repositoryResponse = artistRepository.findById((String) artistData.get("id"));
        if (repositoryResponse.isPresent()) {
            return repositoryResponse.get();
        } else {
            Artist artist = new Artist();
            artist.setFollowers((Integer) ((Map<String, Object>) artistData.get("followers")).get("total"));
            artist.setGenres((List<String>) artistData.get("genres"));
            artist.setHref((String) artistData.get("href"));
            artist.setId((String) artistData.get("id"));
            artist.setImages(spotifyImageService.createSpotifyImageListFromJSON((List<Map<String, Object>>) artistData.get("images")));
            artist.setName((String) artistData.get("name"));
            artist.setPopularity((Integer) artistData.get("popularity"));
            artist.setUri((String) artistData.get("uri"));
            return artistRepository.save(artist);
        }
    }
}
