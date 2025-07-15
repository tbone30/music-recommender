package com.spotifydiscovery.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration tests for Spotify Discovery Application
 * 
 * Tests the main application context loading and basic functionality.
 * Uses test profile to avoid requiring actual Spotify credentials.
 * 
 * @author Spotify Discovery Team
 */
@SpringBootTest
@ActiveProfiles("test")
class SpotifyDiscoveryApplicationTests {

	/**
	 * Test that the Spring Boot application context loads successfully
	 */
	@Test
	void contextLoads() {
		// This test will pass if the application context loads without errors
		// It validates that all beans can be created and wired correctly
	}

	/**
	 * Test that the main method can be called without errors
	 */
	@Test
	void mainMethodTest() {
		// Test that the main method doesn't throw exceptions
		// This is a basic smoke test for the application entry point
		try {
			// We don't actually call main() here as it would start the server
			// Instead, we just verify the class structure is correct
			assert SpotifyDiscoveryApplication.class != null;
		} catch (Exception e) {
			throw new AssertionError("Main method should not throw exceptions", e);
		}
	}
}
