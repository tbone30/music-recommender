package com.musicrecommender.backend.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import com.musicrecommender.backend.entity.Artist;
import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.service.AlbumService;
import com.musicrecommender.backend.repository.ArtistRepository;
import com.musicrecommender.backend.factory.ArtistFactory;

@Service
public class ArtistService {
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ArtistFactory artistFactory;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private SpotifyIntegrationService spotifyIntegrationService;

    public Mono<Artist> getArtist(String id) {
        return Mono.fromCallable(() -> artistRepository.findById(id).orElse(null));
    }

    public Mono<Artist> saveArtist(Artist artist) {
        return Mono.fromCallable(() -> artistRepository.save(artist));
    }

    public Mono<List<Album>> getArtistAlbums(String artistId) {
        return spotifyIntegrationService.getArtistAlbums(artistId)
            .flatMap(albums -> {
                if((String) albums.get("next") != null) {
                    // If there are more pages, fetch all albums
                    return spotifyIntegrationService.fetchAllAlbumsForArtist(albums)
                        .flatMap(allAlbums -> albumService.createAlbumListFromJSONSimple((List<Map<String, Object>>) allAlbums));
                } else {
                    // No more pages, return the current list
                    return albumService.createAlbumListFromJSONSimple((List<Map<String, Object>>) albums.get("items"));
                }
            });
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
                    return spotifyIntegrationService.getArtist(artistId)
                                                    .flatMap(artist -> createArtistFromJSON(artist));
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
        return spotifyIntegrationService.getSeveralArtists(idsCommaSeparated)
                                        .flatMap(artists -> createArtistListFromJSON(artists));
    }
}
