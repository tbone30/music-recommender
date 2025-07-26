package com.musicrecommender.backend.controller;

import com.musicrecommender.backend.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.musicrecommender.backend.entity.Artist;
import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.entity.Track;
import com.musicrecommender.backend.dto.simplified.SimplifiedAlbumDTO;
import com.musicrecommender.backend.dto.ArtistDTO;
import com.musicrecommender.backend.dto.AlbumDTO;
import com.musicrecommender.backend.dto.TrackDTO;
import com.musicrecommender.backend.factory.DTOFactory;
import java.util.List;

import java.util.Map;

@RestController
@RequestMapping("artists")
@CrossOrigin(origins = "*")
public class ArtistController {
    @Autowired
    private ArtistService artistService;
    @Autowired
    private DTOFactory dtoFactory;

    @GetMapping("/{artistId}")
    public Mono<ArtistDTO> getArtist(@PathVariable String artistId) {
        return artistService.getArtist(artistId)
            .flatMap(artist -> dtoFactory.createArtistDTO(artist));
    }

    @GetMapping("/{artistId}/albums")
    public Mono<List<SimplifiedAlbumDTO>> getArtistAlbums(@PathVariable String artistId) {
        return artistService.getArtistAlbums(artistId)
            .flatMapMany(albums -> reactor.core.publisher.Flux.fromIterable(albums))
            .flatMap(dtoFactory::createSimplifiedAlbumDTO)
            .collectList();
    }

    @GetMapping
    public Mono<List<ArtistDTO>> getSeveralArtists(@RequestParam String ids) {
        return artistService.getSeveralArtists(ids)
            .flatMapMany(artists -> reactor.core.publisher.Flux.fromIterable(artists))
            .flatMap(dtoFactory::createArtistDTO)
            .collectList();
    }

    @GetMapping("/{artistId}/top-tracks")
    public Mono<List<TrackDTO>> getArtistTopTracks(@PathVariable String artistId) {
        return artistService.getArtistTopTracks(artistId)
            .flatMapMany(tracks -> reactor.core.publisher.Flux.fromIterable(tracks))
            .flatMap(dtoFactory::createTrackDTO)
            .collectList();
    }
}
