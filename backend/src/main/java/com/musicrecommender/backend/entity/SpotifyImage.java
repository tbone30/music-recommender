package com.musicrecommender.backend.entity;

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
