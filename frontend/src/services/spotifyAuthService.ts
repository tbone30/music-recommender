// TypeScript Spotify Auth Service
interface SpotifyTokens {
  access_token: string;
  refresh_token: string;
  expires_in: number;
  token_type: string;
  scope: string;
}

interface SpotifyUser {
  id: string;
  display_name: string;
  email: string;
  images: Array<{
    url: string;
    height: number;
    width: number;
  }>;
}

class SpotifyAuthService {
  private apiBaseUrl: string;

  constructor(apiBaseUrl = 'http://localhost:8080/api') {
    this.apiBaseUrl = apiBaseUrl;
  }

  // Store tokens securely
  setTokens(accessToken: string, refreshToken: string, expiresIn: number): void {
    const expiryTime = Date.now() + (expiresIn * 1000);
    localStorage.setItem('spotify_access_token', accessToken);
    localStorage.setItem('spotify_refresh_token', refreshToken);
    localStorage.setItem('spotify_token_expiry', expiryTime.toString());
  }

  // Get valid access token (auto-refresh if needed)
  async getValidAccessToken(): Promise<string> {
    const accessToken = localStorage.getItem('spotify_access_token');
    const expiryTime = localStorage.getItem('spotify_token_expiry');
    
    if (!accessToken || Date.now() > (parseInt(expiryTime || '0') - 60000)) {
      return await this.refreshAccessToken();
    }
    
    return accessToken;
  }

  // Refresh access token
  async refreshAccessToken(): Promise<string> {
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

    const data: SpotifyTokens = await response.json();
    this.setTokens(
      data.access_token, 
      data.refresh_token || refreshToken, 
      data.expires_in
    );
    return data.access_token;
  }

  // Initialize Spotify auth
  async initiateSpotifyAuth(): Promise<void> {
    try {
      const response = await fetch(`${this.apiBaseUrl}/auth/spotify/login`);
      const data = await response.json();
      
      // Redirect to Spotify
      window.location.href = data.authUrl;
    } catch (error) {
      console.error('Failed to initiate Spotify auth:', error);
      throw error;
    }
  }

  // Handle callback from Spotify
  async handleCallback(code: string, state?: string): Promise<SpotifyTokens> {
    const response = await fetch(`${this.apiBaseUrl}/auth/spotify/exchange`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ code })
    });
    
    if (!response.ok) {
      throw new Error('Failed to exchange code for token');
    }

    const data: SpotifyTokens = await response.json();
    this.setTokens(data.access_token, data.refresh_token, data.expires_in);
    
    return data;
  }

  // Make authenticated API calls
  async makeAuthenticatedRequest(endpoint: string, options: RequestInit = {}): Promise<Response> {
    const accessToken = await this.getValidAccessToken();
    
    return fetch(`${this.apiBaseUrl}${endpoint}`, {
      ...options,
      headers: {
        ...options.headers,
        'Authorization': `Bearer ${accessToken}`
      }
    });
  }

  // Get user profile
  async getUserProfile(): Promise<SpotifyUser> {
    const response = await this.makeAuthenticatedRequest('/me');
    if (!response.ok) {
      throw new Error('Failed to get user profile');
    }
    return response.json();
  }

  // Get user playlists
  async getUserPlaylists(): Promise<any> {
    const response = await this.makeAuthenticatedRequest('/me/playlists');
    if (!response.ok) {
      throw new Error('Failed to get user playlists');
    }
    return response.json();
  }

  // Get user top tracks
  async getUserTopTracks(): Promise<any> {
    const response = await this.makeAuthenticatedRequest('/me/top/tracks');
    if (!response.ok) {
      throw new Error('Failed to get user top tracks');
    }
    return response.json();
  }

  // Check if user is connected
  isConnected(): boolean {
    const accessToken = localStorage.getItem('spotify_access_token');
    const refreshToken = localStorage.getItem('spotify_refresh_token');
    return !!(accessToken && refreshToken);
  }

  // Disconnect (clear tokens)
  disconnect(): void {
    localStorage.removeItem('spotify_access_token');
    localStorage.removeItem('spotify_refresh_token');
    localStorage.removeItem('spotify_token_expiry');
  }

  // Validate current token
  async validateToken(): Promise<boolean> {
    try {
      const accessToken = localStorage.getItem('spotify_access_token');
      if (!accessToken) return false;

      const response = await fetch(`${this.apiBaseUrl}/auth/spotify/validate`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ accessToken })
      });

      const data = await response.json();
      return data.valid;
    } catch (error) {
      console.error('Token validation failed:', error);
      return false;
    }
  }
}

export default SpotifyAuthService;
export type { SpotifyTokens, SpotifyUser };
