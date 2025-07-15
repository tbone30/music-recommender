package com.spotifydiscovery.backend.config;

import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Database Configuration for Spotify Discovery Platform
 * 
 * Ensures the SQLite database directory exists and handles initialization.
 * 
 * @author Spotify Discovery Team
 */
@Configuration
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    /**
     * Ensures the database directory exists before the application starts
     */
    @PostConstruct
    public void ensureDatabaseDirectoryExists() {
        try {
            Path dataDir = Paths.get("data");
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
                logger.info("Created database directory: {}", dataDir.toAbsolutePath());
            } else {
                logger.info("Database directory already exists: {}", dataDir.toAbsolutePath());
            }
        } catch (Exception e) {
            logger.error("Failed to create database directory", e);
        }
    }
}
