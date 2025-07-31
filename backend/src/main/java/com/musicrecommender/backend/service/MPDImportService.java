package com.musicrecommender.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musicrecommender.backend.entity.mpd.MPDPlaylist;
import com.musicrecommender.backend.entity.mpd.MPDTrack;
import com.musicrecommender.backend.entity.SpotifyImage;
import com.musicrecommender.backend.repository.MPDPlaylistRepository;
import com.musicrecommender.backend.repository.MPDTrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class MPDImportService {
    @Autowired
    private MPDPlaylistRepository playlistRepository;
    @Autowired
    private MPDTrackRepository trackRepository;

        /**
     * Processes all MPD slice files in the given directory, calling importMPD for each file.
     * Logs the success or failure of each slice as it goes.
     * @param slicesRootDir The root directory containing MPD slice files.
     */
    public void importAllSlices(File slicesRootDir) {
        if (!slicesRootDir.isDirectory()) {
            System.out.println("Provided path is not a directory: " + slicesRootDir.getAbsolutePath());
            return;
        }
        File[] files = slicesRootDir.listFiles((dir, name) -> name.startsWith("mpd.slice") && name.endsWith(".json"));
        if (files == null || files.length == 0) {
            System.out.println("No MPD slice files found in directory: " + slicesRootDir.getAbsolutePath());
            return;
        }
        for (File sliceFile : files) {
            boolean success = importMPD(sliceFile);
            String result = success ? "SUCCESS" : "FAIL";
            System.out.println("Processed slice: " + sliceFile.getName() + " - Result: " + result);
        }
    }

    public boolean importMPD(File mpdJsonFile) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(mpdJsonFile);
            JsonNode playlists = root.get("playlists");
            if (playlists == null) {
                // If the file is a slice, playlists are inside the top-level object under "playlists"
                playlists = root.path("playlists");
            }

            for (JsonNode playlistNode : playlists) {
                MPDPlaylist playlist = new MPDPlaylist();
                playlist.setId(playlistNode.path("pid").asLong());
                playlist.setName(playlistNode.path("name").asText(null));

                List<MPDTrack> tracks = new ArrayList<>();
                for (JsonNode trackNode : playlistNode.path("tracks")) {
                    String trackUri = trackNode.path("track_uri").asText(null);
                    String trackId = trackUri != null ? trackUri.substring(trackUri.lastIndexOf(":") + 1) : null;
                    String artistUri = trackNode.path("artist_uri").asText(null);
                    String artistId = artistUri != null ? artistUri.substring(artistUri.lastIndexOf(":") + 1) : null;
                    String albumUri = trackNode.path("album_uri").asText(null);
                    String albumId = albumUri != null ? albumUri.substring(albumUri.lastIndexOf(":") + 1) : null;

                    MPDTrack track = trackRepository.findById(trackId).orElseGet(() -> {
                        MPDTrack t = new MPDTrack();
                        t.setId(trackId);
                        t.setArtistId(artistId);
                        t.setAlbumId(albumId);
                        return trackRepository.save(t);
                    });
                    tracks.add(track);
                }
                playlist.setTracks(tracks);

                playlistRepository.save(playlist);
            }
            return true;
        } catch (Exception e) {
            // Optionally log the error
            return false;
        }
    }

        /**
     * Checks which MPD slice files in the directory have already been processed (i.e., playlists exist in DB).
     * Prints the processed slice file names.
     */
    public void printProcessedSlices(File slicesRootDir) {
        if (!slicesRootDir.isDirectory()) {
            System.out.println("Provided path is not a directory: " + slicesRootDir.getAbsolutePath());
            return;
        }
        File[] files = slicesRootDir.listFiles((dir, name) -> name.startsWith("mpd.slice") && name.endsWith(".json"));
        if (files == null || files.length == 0) {
            System.out.println("No MPD slice files found in directory: " + slicesRootDir.getAbsolutePath());
            return;
        }
        for (File sliceFile : files) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(sliceFile);
                JsonNode playlists = root.get("playlists");
                if (playlists == null) {
                    playlists = root.path("playlists");
                }
                boolean found = false;
                for (JsonNode playlistNode : playlists) {
                    long pid = playlistNode.path("pid").asLong();
                    if (playlistRepository.existsById(pid)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    System.out.println("Processed slice: " + sliceFile.getName());
                }
            } catch (Exception e) {
                System.out.println("Error checking slice: " + sliceFile.getName());
            }
        }
    }

        /**
     * Prints all MPDPlaylist entries in the database (ID and name).
     */
    public void printAllPlaylistsInDb() {
        List<MPDPlaylist> playlists = playlistRepository.findAll();
        if (playlists.isEmpty()) {
            System.out.println("No playlists found in database.");
            return;
        }
        System.out.println("Playlists in database:");
        for (MPDPlaylist playlist : playlists) {
            System.out.println("ID: " + playlist.getId() + ", Name: " + playlist.getName());
            List<MPDTrack> tracks = playlist.getTracks();
            if (tracks != null && !tracks.isEmpty()) {
                MPDTrack firstTrack = tracks.get(0);
                System.out.println("  First Track: ID=" + firstTrack.getId()
                    + ", ArtistID=" + firstTrack.getArtistId()
                    + (firstTrack.getAlbumId() != null ? ", AlbumID=" + firstTrack.getAlbumId() : ""));
            } else {
                System.out.println("  No tracks in this playlist.");
            }
        }
    }
}