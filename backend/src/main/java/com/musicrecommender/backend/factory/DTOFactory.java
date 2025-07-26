package com.musicrecommender.backend.factory;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.musicrecommender.backend.service.AlbumService;
import com.musicrecommender.backend.service.TrackService;
import com.musicrecommender.backend.service.PlaylistService;
import com.musicrecommender.backend.service.SpotifyIntegrationService;
import com.musicrecommender.backend.dto.*;
import com.musicrecommender.backend.dto.simplified.*;
import com.musicrecommender.backend.entity.*;
import com.musicrecommender.backend.entity.simplified.*;

@Component
public class DTOFactory {
    @Autowired
    @Lazy
    private AlbumService albumService;
    @Autowired
    @Lazy
    private TrackService trackService;
    @Autowired
    @Lazy
    private PlaylistService playlistService;
    @Autowired
    @Lazy
    private SpotifyIntegrationService spotifyIntegrationService;

    public Mono<AlbumDTO> createAlbumDTO(Album album) {
        if (album == null) return Mono.empty();

        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setId(album.getId());
        albumDTO.setName(album.getName());
        albumDTO.setTotalTracks(album.getTotalTracks());
        albumDTO.setPopularity(album.getPopularity());
        albumDTO.setHref(album.getHref());
        albumDTO.setReleaseDate(album.getReleaseDate());
        albumDTO.setReleaseDatePrecision(album.getReleaseDatePrecision());
        albumDTO.setAlbumType(album.getAlbumType());
        albumDTO.setUri(album.getUri());

        Mono<List<ArtistDTO>> artistsMono;
        if (album.getArtists() != null) {
            artistsMono = reactor.core.publisher.Flux.fromIterable(album.getArtists())
                .flatMap(this::createArtistDTO)
                .collectList();
        } else {
            artistsMono = Mono.just(List.of());
        }

        Mono<List<TrackDTO>> tracksMono;
        if (album.getTracks() != null) {
            tracksMono = reactor.core.publisher.Flux.fromIterable(album.getTracks())
                .flatMap(track -> {
                    List<SpotifyImageDTO> images = album.getImages() != null ? album.getImages().stream().map(SpotifyImageDTO::new).collect(Collectors.toList()) : List.of();
                    return createTrackDTO(track, images, album.getName());
                })
                .collectList();
        } else {
            tracksMono = Mono.just(List.of());
        }

        if (album.getImages() != null) {
            albumDTO.setImages(album.getImages().stream()
                .map(SpotifyImageDTO::new)
                .collect(Collectors.toList()));
        }

        return artistsMono.zipWith(tracksMono)
            .map(tuple -> {
                List<ArtistDTO> artistDTOs = tuple.getT1();
                List<TrackDTO> trackDTOs = tuple.getT2();
                albumDTO.setArtists(artistDTOs);
                albumDTO.setTracks(trackDTOs);
                return albumDTO;
            });
    }

    public Mono<ArtistDTO> createArtistDTO(Artist artist) {
        if (artist == null) return Mono.empty();

        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setId(artist.getId());
        artistDTO.setName(artist.getName());
        artistDTO.setPopularity(artist.getPopularity());
        artistDTO.setHref(artist.getHref());
        artistDTO.setUri(artist.getUri());

        if (artist.getImages() != null) {
            artistDTO.setImages(artist.getImages().stream()
                .map(SpotifyImageDTO::new)
                .collect(Collectors.toList()));
        }

        return Mono.just(artistDTO);
    }

    public Mono<TrackDTO> createTrackDTO(Track track) {
        if (track == null) return Mono.empty();

        TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId(track.getId());
        trackDTO.setName(track.getName());
        trackDTO.setHref(track.getHref());
        trackDTO.setUri(track.getUri());
        trackDTO.setDurationMs(track.getDuration());
        trackDTO.setExplicit(track.isExplicit());
        trackDTO.setPopularity(track.getPopularity());
        trackDTO.setAlbumId(track.getAlbumId());

        Mono<List<ArtistDTO>> artistsMono;
        if (track.getArtists() != null) {
            artistsMono = reactor.core.publisher.Flux.fromIterable(track.getArtists())
                .flatMap(this::createArtistDTO)
                .collectList();
        } else {
            artistsMono = Mono.just(List.of());
        }

        return artistsMono.flatMap(artistDTOs -> {
            trackDTO.setArtists(artistDTOs);
            return albumService.getAlbum(track.getAlbumId())
                .map(album -> {
                    if (album != null) {
                        trackDTO.setAlbumImages(album.getImages().stream()
                            .map(SpotifyImageDTO::new)
                            .collect(Collectors.toList()));
                        trackDTO.setAlbumName(album.getName());
                    } else {
                        trackDTO.setAlbumImages(List.of());
                        trackDTO.setAlbumName(null);
                    }
                    return trackDTO;
                });
        });
    }

    public Mono<TrackDTO> createTrackDTO(Track track, List<SpotifyImageDTO> albumImages, String albumName) {
        if (track == null) return Mono.empty();

        TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId(track.getId());
        trackDTO.setName(track.getName());
        trackDTO.setHref(track.getHref());
        trackDTO.setUri(track.getUri());
        trackDTO.setDurationMs(track.getDuration());
        trackDTO.setExplicit(track.isExplicit());
        trackDTO.setPopularity(track.getPopularity());
        trackDTO.setAlbumId(track.getAlbumId());

        Mono<List<ArtistDTO>> artistsMono;
        if (track.getArtists() != null) {
            artistsMono = reactor.core.publisher.Flux.fromIterable(track.getArtists())
                .flatMap(this::createArtistDTO)
                .collectList();
        } else {
            artistsMono = Mono.just(List.of());
        }

        return artistsMono.map(artistDTOs -> {
            trackDTO.setArtists(artistDTOs);
            trackDTO.setAlbumImages(albumImages);
            trackDTO.setAlbumName(albumName);
            return trackDTO;
        });
    }

    public Mono<PlaylistDTO> createPlaylistDTO(Playlist playlist) {
        if (playlist == null) return Mono.empty();

        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setCollaborative(playlist.getCollaborative());
        playlistDTO.setDescription(playlist.getDescription());
        playlistDTO.setHref(playlist.getHref());
        playlistDTO.setId(playlist.getId());
        playlistDTO.setImages(playlist.getImages().stream()
            .map(SpotifyImageDTO::new)
            .collect(Collectors.toList()));
        playlistDTO.setName(playlist.getName());
        playlistDTO.setOwnerId(playlist.getOwnerId());
        playlistDTO.setOwnerDisplayName(playlist.getOwnerDisplayName());
        playlistDTO.setIsPublic(playlist.getIsPublic());

        if (playlist.getTracks() != null && !playlist.getTracks().isEmpty()) {
            // 1. Collect unique album IDs
            List<Track> tracks = playlist.getTracks();
            List<String> albumIds = tracks.stream()
                .map(Track::getAlbumId)
                .filter(id -> id != null && !id.isEmpty())
                .distinct()
                .collect(Collectors.toList());

            // 2. Batch fetch albums
            String joinedIds = String.join(",", albumIds);
            return albumService.getSeveralAlbums(joinedIds)
                .flatMapMany(albums -> {
                    // Map albumId -> Album
                    java.util.Map<String, Album> albumMap = albums.stream()
                        .collect(Collectors.toMap(Album::getId, a -> a));
                    // 3. For each track, build TrackDTO using the album from the map
                    return reactor.core.publisher.Flux.fromIterable(tracks)
                        .flatMap(track -> {
                            Album album = albumMap.get(track.getAlbumId());
                            List<SpotifyImageDTO> albumImages = album != null && album.getImages() != null
                                ? album.getImages().stream().map(SpotifyImageDTO::new).collect(Collectors.toList())
                                : List.of();
                            String albumName = album != null ? album.getName() : null;
                            return createTrackDTO(track, albumImages, albumName);
                        });
                })
                .collectList()
                .map(trackDTOs -> {
                    playlistDTO.setTracks(trackDTOs);
                    playlistDTO.setUri(playlist.getUri());
                    return playlistDTO;
                });
        } else {
            playlistDTO.setUri(playlist.getUri());
            return Mono.just(playlistDTO);
        }
    }

    public Mono<SimplifiedAlbumDTO> createSimplifiedAlbumDTO(SimplifiedAlbum simplifiedAlbum) {
        if (simplifiedAlbum == null) return Mono.empty();

        SimplifiedAlbumDTO simplifiedAlbumDTO = new SimplifiedAlbumDTO();
        simplifiedAlbumDTO.setAlbumType(simplifiedAlbum.getAlbumType());
        simplifiedAlbumDTO.setTotalTracks(simplifiedAlbum.getTotalTracks());
        simplifiedAlbumDTO.setId(simplifiedAlbum.getId());
        simplifiedAlbumDTO.setName(simplifiedAlbum.getName());
        simplifiedAlbumDTO.setHref(simplifiedAlbum.getHref());
        simplifiedAlbumDTO.setReleaseDate(simplifiedAlbum.getReleaseDate());
        simplifiedAlbumDTO.setReleaseDatePrecision(simplifiedAlbum.getReleaseDatePrecision());
        simplifiedAlbumDTO.setUri(simplifiedAlbum.getUri());

        if (simplifiedAlbum.getImages() != null) {
            simplifiedAlbumDTO.setImages(simplifiedAlbum.getImages().stream()
                .map(SpotifyImageDTO::new)
                .collect(Collectors.toList()));
        }

        if (simplifiedAlbum.getArtists() != null) {
            // Await all Mono<SimplifiedArtistDTO> and collect as List<SimplifiedArtistDTO>
            return reactor.core.publisher.Flux.fromIterable(simplifiedAlbum.getArtists())
                .flatMap(this::createSimplifiedArtistDTO)
                .collectList()
                .map(artistDTOs -> {
                    simplifiedAlbumDTO.setArtists(artistDTOs);
                    return simplifiedAlbumDTO;
                });
        } else {
            simplifiedAlbumDTO.setArtists(List.of());
            return Mono.just(simplifiedAlbumDTO);
        }
    }

    public Mono<SimplifiedAlbumDTO> createSimplifiedAlbumDTO(Map<String, Object> albumData) {
        if (albumData == null) return Mono.empty();

        SimplifiedAlbumDTO simplifiedAlbumDTO = new SimplifiedAlbumDTO();
        simplifiedAlbumDTO.setId((String) albumData.get("id"));
        simplifiedAlbumDTO.setName((String) albumData.get("name"));
        simplifiedAlbumDTO.setTotalTracks((Integer) albumData.get("total_tracks"));
        simplifiedAlbumDTO.setHref((String) albumData.get("href"));
        simplifiedAlbumDTO.setReleaseDate((String) albumData.get("release_date"));
        simplifiedAlbumDTO.setReleaseDatePrecision((String) albumData.get("release_date_precision"));
        simplifiedAlbumDTO.setUri((String) albumData.get("uri"));

        List<Map<String, Object>> imagesData = (List<Map<String, Object>>) albumData.get("images");
        if (imagesData != null) {
            simplifiedAlbumDTO.setImages(imagesData.stream()
                .map(imgMap -> {
                    SpotifyImageDTO dto = new SpotifyImageDTO();
                    dto.setHeight(imgMap.get("height") != null ? ((Number) imgMap.get("height")).intValue() : null);
                    dto.setWidth(imgMap.get("width") != null ? ((Number) imgMap.get("width")).intValue() : null);
                    dto.setUrl((String) imgMap.get("url"));
                    return dto;
                })
                .collect(Collectors.toList()));
        }

        List<Map<String, Object>> artistsData = (List<Map<String, Object>>) albumData.get("artists");
        if (artistsData != null) {
            return reactor.core.publisher.Flux.fromIterable(artistsData)
                .flatMap(this::createSimplifiedArtistDTO)
                .collectList()
                .map(artistDTOs -> {
                    simplifiedAlbumDTO.setArtists(artistDTOs);
                    return simplifiedAlbumDTO;
                });
        }

        return Mono.just(simplifiedAlbumDTO);
    }

    public Mono<SimplifiedArtistDTO> createSimplifiedArtistDTO(SimplifiedArtist artist) {
        if (artist == null) return Mono.empty();

        SimplifiedArtistDTO simplifiedArtistDTO = new SimplifiedArtistDTO();
        simplifiedArtistDTO.setId(artist.getId());
        simplifiedArtistDTO.setName(artist.getName());
        simplifiedArtistDTO.setHref(artist.getHref());
        simplifiedArtistDTO.setUri(artist.getUri());

        return Mono.just(simplifiedArtistDTO);
    }

    public Mono<SimplifiedArtistDTO> createSimplifiedArtistDTO(Map<String, Object> artistData) {
        if (artistData == null) return Mono.empty();

        SimplifiedArtistDTO simplifiedArtistDTO = new SimplifiedArtistDTO();
        simplifiedArtistDTO.setId((String) artistData.get("id"));
        simplifiedArtistDTO.setName((String) artistData.get("name"));
        simplifiedArtistDTO.setHref((String) artistData.get("href"));
        simplifiedArtistDTO.setUri((String) artistData.get("uri"));

        return Mono.just(simplifiedArtistDTO);
    }
}
