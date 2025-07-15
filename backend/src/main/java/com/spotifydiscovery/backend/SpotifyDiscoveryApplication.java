package com.spotifydiscovery.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main Spring Boot Application class for Spotify Discovery Platform
 * 
 * Features:
 * - RESTful API for music discovery
 * - Spotify Web API integration
 * - OAuth2 authentication
 * - Machine learning recommendations
 * - SQLite/PostgreSQL database support
 * 
 * @author Spotify Discovery Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableAsync
@EnableJpaAuditing
public class SpotifyDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotifyDiscoveryApplication.class, args);
    }
}
