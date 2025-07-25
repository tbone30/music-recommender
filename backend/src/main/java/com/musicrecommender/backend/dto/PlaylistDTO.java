package com.musicrecommender.backend.dto;

import java.util.List;

import com.musicrecommender.backend.entity.Playlist;
import com.musicrecommender.backend.entity.SpotifyImage;
import com.musicrecommender.backend.dto.TrackDTO;

public class PlaylistDTO {
    private Boolean collaborative;
    private String description;
    private String href;
    private String id;
    private List<SpotifyImageDTO> images;
    private String name;
    private String ownerId;
    private String ownerDisplayName;
    private Boolean isPublic;
    private List<TrackDTO> tracks;
    private String uri;

    public PlaylistDTO() {
        // Default constructor
    }

    public Boolean getCollaborative() {
        return collaborative;
    }
    public void setCollaborative(Boolean collaborative) {
        this.collaborative = collaborative;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getHref() {
        return href;
    }
    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public List<SpotifyImageDTO> getImages() {
        return images;
    }
    public void setImages(List<SpotifyImageDTO> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerDisplayName() {
        return ownerDisplayName;
    }
    public void setOwnerDisplayName(String ownerDisplayName) {
        this.ownerDisplayName = ownerDisplayName;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }
    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public List<TrackDTO> getTracks() {
        return tracks;
    }
    public void setTracks(List<TrackDTO> tracks) {
        this.tracks = tracks;
    }

    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
}
