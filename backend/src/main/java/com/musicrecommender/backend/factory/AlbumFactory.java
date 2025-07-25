package com.musicrecommender.backend.factory;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;

import com.musicrecommender.backend.entity.Artist;
import com.musicrecommender.backend.entity.Track;
import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.entity.SpotifyImage;
import com.musicrecommender.backend.service.ArtistService;
import com.musicrecommender.backend.service.TrackService;

import reactor.core.publisher.Mono;

@Component
public class AlbumFactory {
    @Autowired
    @Lazy
    private ArtistService artistService;
    @Autowired
    @Lazy
    private TrackService trackService;

    public Mono<Album> createAlbumFromJSON(Map<String, Object> albumData) {
        Album album = new Album();
        album.setAlbumType((String) albumData.get("album_type"));
        album.setTotalTracks((Integer) albumData.get("total_tracks"));
        album.setHref((String) albumData.get("href"));
        album.setId((String) albumData.get("id"));
        album.setImages(SpotifyImage.createSpotifyImageListFromJSON((List<Map<String, Object>>) albumData.get("images")));
        album.setName((String) albumData.get("name"));
        album.setReleaseDate((String) albumData.get("release_date"));
        album.setReleaseDatePrecision((String) albumData.get("release_date_precision"));
        album.setUri((String) albumData.get("uri"));
        album.setPopularity((Integer) albumData.get("popularity"));

        List<Map<String, Object>> artistsData = (List<Map<String, Object>>) albumData.get("artists");
        Mono<List<Artist>> artistsMono = 
            artistsData != null ? artistService.createArtistListFromJSONSimple(artistsData) : Mono.just(List.of());

        List<Map<String, Object>> tracksData = (List<Map<String, Object>>) ((Map<String, Object>) albumData.get("tracks")).get("items");
        if (tracksData == null || tracksData.isEmpty()) {
            return artistsMono.map(artists -> {
                album.setArtists(artists);
                album.setTracks(List.of());
                return album;
            });
        }

        // 1. Collect all unique artist IDs from all tracks
        java.util.Set<String> allArtistIds = new java.util.HashSet<>();
        for (Map<String, Object> track : tracksData) {
            List<Map<String, Object>> tArtists = (List<Map<String, Object>>) track.get("artists");
            if (tArtists != null) {
                for (Map<String, Object> a : tArtists) {
                    String id = (String) a.get("id");
                    if (id != null && !id.isEmpty()) allArtistIds.add(id);
                }
            }
        }

        // 2. Batch fetch all unique artists
        String allArtistIdsCsv = String.join(",", allArtistIds);
        Mono<List<Artist>> allArtistsMono = allArtistIds.isEmpty()
            ? Mono.just(List.of())
            : artistService.getSeveralArtists(allArtistIdsCsv);

        // 3. For each track, build the list of Artist objects it needs
        Mono<List<List<Artist>>> artistsForTracksMono = allArtistsMono.map(allArtists -> {
            java.util.Map<String, Artist> artistMap = new java.util.HashMap<>();
            for (Artist a : allArtists) {
                artistMap.put(a.getId(), a);
            }
            List<List<Artist>> result = new java.util.ArrayList<>();
            for (Map<String, Object> track : tracksData) {
                List<Map<String, Object>> tArtists = (List<Map<String, Object>>) track.get("artists");
                List<Artist> resolved = new java.util.ArrayList<>();
                if (tArtists != null) {
                    for (Map<String, Object> a : tArtists) {
                        String id = (String) a.get("id");
                        if (id != null && artistMap.containsKey(id)) {
                            resolved.add(artistMap.get(id));
                        }
                    }
                }
                result.add(resolved);
            }
            return result;
        });

        // 4. Create tracks with resolved artist lists
        Mono<List<Track>> tracksMono = artistsForTracksMono.flatMap(artistsForTracks ->
            trackService.createTrackListFromJSONSimple(tracksData, artistsForTracks)
        );

        // 5. Zip album artists and tracks
        return Mono.zip(artistsMono, tracksMono)
            .map(tuple -> {
                album.setArtists(tuple.getT1()); // Set album-level artists
                album.setTracks(tuple.getT2());  // Set tracks with resolved artists
                return album;
            });
    }
}
