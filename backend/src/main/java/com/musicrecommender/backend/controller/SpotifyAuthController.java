package com.musicrecommender.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.musicrecommender.backend.service.SpotifyAuthService;
import com.musicrecommender.backend.dto.SpotifyTokenResponse;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth/spotify")
@CrossOrigin(origins = "*")
public class SpotifyAuthController {
    
    @Autowired
    private SpotifyAuthService spotifyAuthService;
    
    @GetMapping("/login")
    public Mono<ResponseEntity<Map<String, Object>>> initiateSpotifyAuth() {
        String state = UUID.randomUUID().toString();
        String authUrl = spotifyAuthService.generateAuthUrl(state);
        
        Map<String, Object> response = new HashMap<>();
        response.put("authUrl", authUrl);
        response.put("state", state);
        
        return Mono.just(ResponseEntity.ok(response));
    }
    
    @PostMapping("/exchange")
    public Mono<ResponseEntity<SpotifyTokenResponse>> exchangeCode(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        
        return spotifyAuthService.exchangeCodeForToken(code)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    @PostMapping("/refresh")
    public Mono<ResponseEntity<SpotifyTokenResponse>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        
        return spotifyAuthService.refreshToken(refreshToken)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
    
    @PostMapping("/validate")
    public Mono<ResponseEntity<Map<String, Object>>> validateToken(@RequestBody Map<String, String> request) {
        String accessToken = request.get("accessToken");
        
        return spotifyAuthService.validateToken(accessToken)
            .map(userData -> {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("user", userData);
                return ResponseEntity.ok(response);
            })
            .onErrorReturn(ResponseEntity.ok(createErrorResponse("Invalid token")));
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("valid", false);
        response.put("message", message);
        return response;
    }
}
