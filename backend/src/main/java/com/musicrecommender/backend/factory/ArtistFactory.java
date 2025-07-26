package com.musicrecommender.backend.factory;

import java.util.List;
import java.util.Map;

import com.musicrecommender.backend.entity.Artist;
import com.musicrecommender.backend.entity.SpotifyImage;
import com.musicrecommender.backend.entity.simplified.SimplifiedArtist;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Component;

@Component
public class ArtistFactory {
    public Mono<Artist> createArtistFromJSON(Map<String, Object> artistData) {
        Artist artist = new Artist();
        artist.setFollowers((Integer) ((Map<String, Object>) artistData.get("followers")).get("total"));
        artist.setGenres((List<String>) artistData.get("genres"));
        artist.setHref((String) artistData.get("href"));
        artist.setId((String) artistData.get("id"));
        artist.setImages(SpotifyImage.createSpotifyImageListFromJSON((List<Map<String, Object>>) artistData.get("images")));
        artist.setName((String) artistData.get("name"));
        artist.setPopularity((Integer) artistData.get("popularity"));
        artist.setUri((String) artistData.get("uri"));
        return Mono.just(artist);
    }

    public Mono<SimplifiedArtist> createSimplifiedArtistFromJSON(Map<String, Object> artistData) {
        SimplifiedArtist simplifiedArtist = new SimplifiedArtist();
        simplifiedArtist.setId((String) artistData.get("id"));
        simplifiedArtist.setName((String) artistData.get("name"));
        simplifiedArtist.setHref((String) artistData.get("href"));
        simplifiedArtist.setUri((String) artistData.get("uri"));

        return Mono.just(simplifiedArtist);
    }
}
