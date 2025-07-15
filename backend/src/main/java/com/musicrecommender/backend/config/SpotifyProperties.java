package com.musicrecommender.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spotify.api")
public class SpotifyProperties {
    private String baseUrl = "https://api.spotify.com/v1";
    private String accountsUrl = "https://accounts.spotify.com";
    private String clientId;
    private String clientSecret;
    private String redirectUri;

    // Getters and setters
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    
    public String getAccountsUrl() { return accountsUrl; }
    public void setAccountsUrl(String accountsUrl) { this.accountsUrl = accountsUrl; }
    
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    
    public String getClientSecret() { return clientSecret; }
    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }
    
    public String getRedirectUri() { return redirectUri; }
    public void setRedirectUri(String redirectUri) { this.redirectUri = redirectUri; }
}
