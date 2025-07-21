package com.musicrecommender.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.musicrecommender.backend.service.SpotifyAuthService;
import com.musicrecommender.backend.dto.SpotifyTokenResponse;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/auth/spotify")
@CrossOrigin(origins = "*")
public class SpotifyAuthController {
    
    @Autowired
    private SpotifyAuthService spotifyAuthService;
    
    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> initiateSpotifyAuth() {
        String state = UUID.randomUUID().toString();
        String authUrl = spotifyAuthService.generateAuthUrl(state);
        
        Map<String, Object> response = new HashMap<>();
        response.put("authUrl", authUrl);
        response.put("state", state);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/exchange")
    public ResponseEntity<SpotifyTokenResponse> exchangeCode(@RequestBody Map<String, String> request) {
        try {
            String code = request.get("code");
            SpotifyTokenResponse tokenResponse = spotifyAuthService.exchangeCodeForToken(code);
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<SpotifyTokenResponse> refreshToken(@RequestBody Map<String, String> request) {
        try {
            String refreshToken = request.get("refreshToken");
            SpotifyTokenResponse tokenResponse = spotifyAuthService.refreshToken(refreshToken);
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestBody Map<String, String> request) {
        try {
            String accessToken = request.get("accessToken");
            Map<String, Object> userData = spotifyAuthService.validateToken(accessToken);
            
            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("user", userData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(createErrorResponse("Invalid token"));
        }
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("valid", false);
        response.put("message", message);
        return response;
    }
}
