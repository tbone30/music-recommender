package com.spotifydiscovery.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for Spotify API integration
 * 
 * Maps application.yml spotify.* properties to Java objects
 * for type-safe configuration access
 * 
 * @author Spotify Discovery Team
 */
@Configuration
@ConfigurationProperties(prefix = "spotify")
public class SpotifyProperties {

    private Api api = new Api();
    private String clientId;
    private String clientSecret;
    private String redirectUri;

    // Getters and Setters
    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    /**
     * Nested configuration for Spotify API URLs
     */
    public static class Api {
        private String baseUrl;
        private String accountsUrl;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getAccountsUrl() {
            return accountsUrl;
        }

        public void setAccountsUrl(String accountsUrl) {
            this.accountsUrl = accountsUrl;
        }
    }
}
