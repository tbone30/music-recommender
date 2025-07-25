package com.musicrecommender.backend.factory;

import java.util.stream.Collectors;
import java.util.List;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.musicrecommender.backend.service.AlbumService;
import com.musicrecommender.backend.service.TrackService;
import com.musicrecommender.backend.service.PlaylistService;
import com.musicrecommender.backend.service.SpotifyIntegrationService;
import com.musicrecommender.backend.dto.*;
import com.musicrecommender.backend.entity.Album;
import com.musicrecommender.backend.entity.Artist;
import com.musicrecommender.backend.entity.Playlist;
import com.musicrecommender.backend.entity.Track;

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

    public AlbumDTO createAlbumDTO(Album album) {
        if (album == null) return null;

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

        if (album.getArtists() != null) {
            albumDTO.setArtists(album.getArtists().stream()
                .map(artist ->  createArtistDTO(artist))
                .collect(Collectors.toList()));
        }

        if (album.getTracks() != null) {
            albumDTO.setTracks(album.getTracks().stream()
                .map(track -> createTrackDTO(track, album.getImages().stream()
                    .map(SpotifyImageDTO::new)
                    .collect(Collectors.toList()), album.getName()))
                .collect(Collectors.toList()));
        }

        if (album.getImages() != null) {
            albumDTO.setImages(album.getImages().stream()
                .map(SpotifyImageDTO::new)
                .collect(Collectors.toList()));
        }

        return albumDTO;
    }

    public ArtistDTO createArtistDTO(Artist artist) {
        if (artist == null) return null;

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

        return artistDTO;
    }

    public TrackDTO createTrackDTO(Track track) {
        if (track == null) return null;

        TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId(track.getId());
        trackDTO.setName(track.getName());
        trackDTO.setHref(track.getHref());
        trackDTO.setUri(track.getUri());
        trackDTO.setDurationMs(track.getDuration());
        trackDTO.setExplicit(track.isExplicit());
        trackDTO.setPopularity(track.getPopularity());

        if (track.getArtists() != null) {
            trackDTO.setArtists(track.getArtists().stream()
                .map(this::createArtistDTO)
                .collect(Collectors.toList()));
        }

        Mono<Album> albumMono = albumService.getAlbum(track.getAlbumId());
        albumMono.subscribe(album -> {
            if (album != null) {
                trackDTO.setAlbumId(album.getId());
                trackDTO.setAlbumImages(album.getImages().stream()
                    .map(SpotifyImageDTO::new)
                    .collect(Collectors.toList()));
                trackDTO.setAlbumName(album.getName());
            } else {
                trackDTO.setAlbumId(null);
                trackDTO.setAlbumImages(List.of());
                trackDTO.setAlbumName(null);
            }
        });

        return trackDTO;
    }

    public TrackDTO createTrackDTO(Track track, List<SpotifyImageDTO> albumImages, String albumName) {
        if (track == null) return null;

        TrackDTO trackDTO = new TrackDTO();
        trackDTO.setId(track.getId());
        trackDTO.setName(track.getName());
        trackDTO.setHref(track.getHref());
        trackDTO.setUri(track.getUri());
        trackDTO.setDurationMs(track.getDuration());
        trackDTO.setExplicit(track.isExplicit());
        trackDTO.setPopularity(track.getPopularity());

        if (track.getArtists() != null) {
            trackDTO.setArtists(track.getArtists().stream()
                .map(this::createArtistDTO)
                .collect(Collectors.toList()));
        }

        trackDTO.setAlbumImages(albumImages);
        trackDTO.setAlbumName(albumName);
        return trackDTO;
    }

    public PlaylistDTO createPlaylistDTO(Playlist playlist) {
        if (playlist == null) return null;

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

        if (playlist.getTracks() != null) {
            playlistDTO.setTracks(playlist.getTracks().stream()
                .map(this::createTrackDTO)
                .collect(Collectors.toList()));
        }

        playlistDTO.setUri(playlist.getUri());

        return playlistDTO;
    }
}
