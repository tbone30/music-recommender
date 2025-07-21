package com.musicrecommender.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.musicrecommender.backend.config.SpotifyProperties;
import com.musicrecommender.backend.dto.SpotifyTokenResponse;
import java.util.Base64;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;

@Service
public class SpotifyAuthService {
    
    @Autowired
    private SpotifyProperties spotifyProperties;
    
    @Autowired
    private WebClient webClient;
    
    public String generateAuthUrl(String state) {
        String scopes = "user-read-private user-read-email playlist-read-private user-top-read user-library-read";
        
        return spotifyProperties.getAccountsUrl() + "/authorize?" +
                "response_type=code" +
                "&client_id=" + spotifyProperties.getClientId() +
                "&scope=" + scopes.replace(" ", "%20") +
                "&redirect_uri=" + spotifyProperties.getRedirectUri() +
                "&state=" + state;
    }
    
    public SpotifyTokenResponse exchangeCodeForToken(String code) {
        String credentials = Base64.getEncoder()
            .encodeToString((spotifyProperties.getClientId() + ":" + spotifyProperties.getClientSecret()).getBytes());
        
        String requestBody = "grant_type=authorization_code" +
                           "&code=" + code +
                           "&redirect_uri=" + spotifyProperties.getRedirectUri();
        
        return webClient.post()
            .uri(spotifyProperties.getAccountsUrl() + "/api/token")
            .header("Authorization", "Basic " + credentials)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(SpotifyTokenResponse.class)
            .block();
    }
    
    public SpotifyTokenResponse refreshToken(String refreshToken) {
        String credentials = Base64.getEncoder()
            .encodeToString((spotifyProperties.getClientId() + ":" + spotifyProperties.getClientSecret()).getBytes());
        
        String requestBody = "grant_type=refresh_token&refresh_token=" + refreshToken;
        
        return webClient.post()
            .uri(spotifyProperties.getAccountsUrl() + "/api/token")
            .header("Authorization", "Basic " + credentials)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(SpotifyTokenResponse.class)
            .block();
    }
    
    public Map<String, Object> validateToken(String accessToken) {
        return webClient.get()
            .uri(spotifyProperties.getBaseUrl() + "/me")
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .block();
    }
}
