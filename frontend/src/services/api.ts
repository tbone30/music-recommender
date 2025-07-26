// Simplified API service for stateless Spotify auth
import SpotifyAuthService from './spotifyAuthService';

interface UserData {
  id: number;
  username: string;
  email: string;
  createdAt: string;
}

interface UserRegistration {
  username: string;
  email: string;
  password: string;
}

interface UserLogin {
  username: string;
  password: string;
}

class ApiService {
  private baseURL: string;
  private spotifyAuth: SpotifyAuthService;

  constructor() {
    this.baseURL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';
    this.spotifyAuth = new SpotifyAuthService(this.baseURL);
  }

  // User Management APIs
  async registerUser(userData: UserRegistration): Promise<any> {
    const response = await fetch(`${this.baseURL}/users/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(userData)
    });
    return response.json();
  }

  async loginUser(credentials: UserLogin): Promise<any> {
    const response = await fetch(`${this.baseURL}/users/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(credentials)
    });
    return response.json();
  }

  async getUser(userId: number): Promise<any> {
    const response = await fetch(`${this.baseURL}/users/${userId}`);
    return response.json();
  }

  // Spotify-authenticated endpoints
  async getPlaylist(playlistId: string): Promise<any> {
    return this.spotifyAuth.getPlaylist(playlistId);
  }

  async getUserSpotifyProfile(): Promise<any> {
    return this.spotifyAuth.getUserProfile();
  }

  async getUserPlaylists(): Promise<any> {
    return this.spotifyAuth.getUserPlaylists();
  }

  async getUserTopTracks(): Promise<any> {
    return this.spotifyAuth.getUserTopTracks();
  }

  async makeAuthenticatedSpotifyRequest(endpoint: string, options: RequestInit = {}): Promise<Response> {
    return this.spotifyAuth.makeAuthenticatedRequest(endpoint, options);
  }

  // Public Spotify endpoints (no auth required)
  async getPublicTrack(trackId: string): Promise<any> {
    const response = await fetch(`${this.baseURL}/tracks/${trackId}`);
    return response.json();
  }

  async getPublicArtist(artistId: string): Promise<any> {
    const response = await fetch(`${this.baseURL}/artists/${artistId}`);
    return response.json();
  }

  async getPublicArtistAlbums(artistId: string): Promise<any> {
    const response = await fetch(`${this.baseURL}/artists/${artistId}/albums`);
    return response.json();
  }

  async getPublicArtistTopTracks(artistId: string): Promise<any> {
    const response = await fetch(`${this.baseURL}/artists/${artistId}/top-tracks`);
    return response.json();
  }

  async getPublicAlbum(albumId: string): Promise<any> {
    const response = await fetch(`${this.baseURL}/albums/${albumId}`);
    return response.json();
  }

  // Utility methods
  isSpotifyConnected(): boolean {
    return this.spotifyAuth.isConnected();
  }

  disconnectSpotify(): void {
    this.spotifyAuth.disconnect();
  }
}

export default ApiService;
export type { UserData, UserRegistration, UserLogin };
