// Example client-side JavaScript for token management
class SpotifyAuthClient {
    constructor(apiBaseUrl = 'http://localhost:8080/api') {
        this.apiBaseUrl = apiBaseUrl;
    }

    // Store tokens securely
    setTokens(accessToken, refreshToken, expiresIn) {
        const expiryTime = Date.now() + (expiresIn * 1000);
        localStorage.setItem('spotify_access_token', accessToken);
        localStorage.setItem('spotify_refresh_token', refreshToken);
        localStorage.setItem('spotify_token_expiry', expiryTime);
    }

    // Get valid access token (auto-refresh if needed)
    async getValidAccessToken() {
        const accessToken = localStorage.getItem('spotify_access_token');
        const expiryTime = localStorage.getItem('spotify_token_expiry');
        
        if (!accessToken || Date.now() > (expiryTime - 60000)) { // Refresh 1 min before expiry
            return await this.refreshAccessToken();
        }
        
        return accessToken;
    }

    // Refresh access token
    async refreshAccessToken() {
        const refreshToken = localStorage.getItem('spotify_refresh_token');
        
        if (!refreshToken) {
            throw new Error('No refresh token available');
        }

        const response = await fetch(`${this.apiBaseUrl}/auth/spotify/refresh`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ refreshToken })
        });
        
        if (!response.ok) {
            throw new Error('Failed to refresh token');
        }

        const data = await response.json();
        this.setTokens(
            data.access_token, 
            data.refresh_token || refreshToken, 
            data.expires_in
        );
        return data.access_token;
    }

    // Initialize Spotify auth
    async initiateSpotifyAuth() {
        const response = await fetch(`${this.apiBaseUrl}/auth/spotify/login`);
        const data = await response.json();
        
        // Store state for validation
        localStorage.setItem('spotify_auth_state', data.state);
        
        // Redirect to Spotify
        window.location.href = data.authUrl;
    }

    // Handle callback from Spotify
    async handleCallback(code, state) {
        const storedState = localStorage.getItem('spotify_auth_state');
        
        if (state !== storedState) {
            throw new Error('Invalid state parameter');
        }

        const response = await fetch(`${this.apiBaseUrl}/auth/spotify/exchange`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ code })
        });
        
        if (!response.ok) {
            throw new Error('Failed to exchange code for token');
        }

        const data = await response.json();
        this.setTokens(data.access_token, data.refresh_token, data.expires_in);
        
        // Clean up
        localStorage.removeItem('spotify_auth_state');
        
        return data;
    }

    // Make authenticated API calls
    async makeAuthenticatedRequest(endpoint, options = {}) {
        const accessToken = await this.getValidAccessToken();
        
        return fetch(`${this.apiBaseUrl}/spotify${endpoint}`, {
            ...options,
            headers: {
                ...options.headers,
                'Authorization': `Bearer ${accessToken}`
            }
        });
    }

    // Get user profile
    async getUserProfile() {
        const response = await this.makeAuthenticatedRequest('/me');
        return response.json();
    }

    // Get user playlists
    async getUserPlaylists() {
        const response = await this.makeAuthenticatedRequest('/me/playlists');
        return response.json();
    }

    // Get user top tracks
    async getUserTopTracks() {
        const response = await this.makeAuthenticatedRequest('/me/top/tracks');
        return response.json();
    }

    // Check if user is connected
    isConnected() {
        const accessToken = localStorage.getItem('spotify_access_token');
        const refreshToken = localStorage.getItem('spotify_refresh_token');
        return !!(accessToken && refreshToken);
    }

    // Disconnect (clear tokens)
    disconnect() {
        localStorage.removeItem('spotify_access_token');
        localStorage.removeItem('spotify_refresh_token');
        localStorage.removeItem('spotify_token_expiry');
        localStorage.removeItem('spotify_auth_state');
    }
}

// Usage example:
const spotifyAuth = new SpotifyAuthClient();

// Initialize auth
document.getElementById('connect-spotify').addEventListener('click', () => {
    spotifyAuth.initiateSpotifyAuth();
});

// Handle callback (on callback page)
const urlParams = new URLSearchParams(window.location.search);
const code = urlParams.get('code');
const state = urlParams.get('state');

if (code && state) {
    spotifyAuth.handleCallback(code, state)
        .then(() => {
            console.log('Successfully connected to Spotify!');
            // Redirect to main app
            window.location.href = '/dashboard';
        })
        .catch(error => {
            console.error('Failed to connect:', error);
        });
}

// Make authenticated requests
if (spotifyAuth.isConnected()) {
    spotifyAuth.getUserProfile()
        .then(profile => console.log('User profile:', profile))
        .catch(error => console.error('Error:', error));
}
