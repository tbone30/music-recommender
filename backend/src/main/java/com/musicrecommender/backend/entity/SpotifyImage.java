package com.musicrecommender.backend.entity;

import java.util.Map;
import java.util.List;

import jakarta.persistence.Embeddable;

@Embeddable
public class SpotifyImage {
    private String url;
    private int width;
    private int height;

    public SpotifyImage() {
        // Default constructor for JPA
    }

    public SpotifyImage(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public SpotifyImage(Map<String, Object> imageData) {
        this.url = (String) imageData.get("url");
        this.width = imageData.get("width") instanceof Integer ? (Integer) imageData.get("width") : -1;
        this.height = imageData.get("height") instanceof Integer ? (Integer) imageData.get("height") : -1;
    }

    public static List<SpotifyImage> createSpotifyImageListFromJSON(List<Map<String, Object>> images) {
        return images.stream()
                     .map(SpotifyImage::new)
                     .toList();
    }

    public String getUrl() {
        return url;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
