package com.musicrecommender.backend.dto;

public class SpotifyImageDTO {
    private String url;
    private Integer height;
    private Integer width;

    public SpotifyImageDTO() {}

    public SpotifyImageDTO(String url, Integer height, Integer width) {
        this.url = url;
        this.height = height;
        this.width = width;
    }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Integer getHeight() { return height; }
    public void setHeight(Integer height) { this.height = height; }

    public Integer getWidth() { return width; }
    public void setWidth(Integer width) { this.width = width; }
}