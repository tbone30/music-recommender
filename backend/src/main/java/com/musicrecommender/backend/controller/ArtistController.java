package com.musicrecommender.backend.controller;

import com.musicrecommender.backend.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.musicrecommender.backend.entity.Artist;
import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.entity.Track;
import java.util.List;

import java.util.Map;

@RestController
@RequestMapping("artists")
@CrossOrigin(origins = "*")
public class ArtistController {
    @Autowired
    private ArtistService artistService;

    @GetMapping("/{artistId}")
    public Mono<Artist> getArtist(@PathVariable String artistId) {
        return artistService.getArtist(artistId);
    }

    @GetMapping("/{artistId}/albums")
    public Mono<List<Album>> getArtistAlbums(@PathVariable String artistId) {
        return artistService.getArtistAlbums(artistId);
    }

    @GetMapping
    public Mono<List<Artist>> getSeveralArtists(@RequestParam String ids) {
        return artistService.getSeveralArtists(ids);
    }

    @GetMapping("/{artistId}/top-tracks")
    public Mono<List<Track>> getArtistTopTracks(@PathVariable String artistId) {
        return artistService.getArtistTopTracks(artistId);
    }
}
