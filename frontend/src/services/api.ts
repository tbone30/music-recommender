import axios, { AxiosInstance, AxiosResponse, AxiosError, InternalAxiosRequestConfig } from 'axios';
import { 
  ApiResponse, 
  PaginatedResponse, 
  User, 
  Track, 
  Playlist, 
  UserTrack,
  RecommendationRequest,
  RecommendationResponse,
  SearchFilters,
  SearchResult
} from '../types';

/**
 * API Service for Spotify Discovery Platform
 * 
 * Centralized service for all API communications with the backend.
 * Handles authentication, error handling, and request/response formatting.
 */
class ApiService {
  private api: AxiosInstance;
  private baseURL: string;

  constructor() {
    this.baseURL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';
    
    this.api = axios.create({
      baseURL: this.baseURL,
      timeout: parseInt(process.env.REACT_APP_API_TIMEOUT || '10000'),
      headers: {
        'Content-Type': 'application/json',
      },
    });

    this.setupInterceptors();
  }

  /**
   * Setup request and response interceptors
   */
  private setupInterceptors(): void {
    // Request interceptor - Add auth token
    this.api.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        const token = localStorage.getItem('spotify_discovery_token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error: AxiosError) => Promise.reject(error)
    );

    // Response interceptor - Handle errors globally
    this.api.interceptors.response.use(
      (response: AxiosResponse) => response,
      (error: AxiosError) => {
        if (error.response?.status === 401) {
          // Token expired or invalid
          localStorage.removeItem('spotify_discovery_token');
          localStorage.removeItem('spotify_discovery_user');
          window.location.href = '/login';
        }
        return Promise.reject(error);
      }
    );
  }

  /**
   * Generic request handler
   */
  private async request<T>(
    method: string,
    url: string,
    data?: any,
    params?: any
  ): Promise<ApiResponse<T>> {
    try {
      const response = await this.api.request<ApiResponse<T>>({
        method,
        url,
        data,
        params,
      });
      return response.data;
    } catch (error) {
      console.error(`API Error [${method.toUpperCase()} ${url}]:`, error);
      throw this.handleError(error as AxiosError);
    }
  }

  /**
   * Error handler
   */
  private handleError(error: AxiosError): Error {
    if (error.response) {
      const message = (error.response.data as any)?.message || 'Server error occurred';
      return new Error(message);
    } else if (error.request) {
      return new Error('Network error - please check your connection');
    } else {
      return new Error('Request configuration error');
    }
  }

  // Authentication Endpoints
  async login(email: string, password: string): Promise<ApiResponse<{ user: User; token: string }>> {
    return this.request('POST', '/auth/login', { email, password });
  }

  async spotifyAuth(code: string): Promise<ApiResponse<{ user: User; token: string }>> {
    return this.request('POST', '/auth/spotify/callback', { code });
  }

  async refreshToken(): Promise<ApiResponse<{ token: string }>> {
    return this.request('POST', '/auth/refresh');
  }

  async logout(): Promise<ApiResponse<void>> {
    return this.request('POST', '/auth/logout');
  }

  async getCurrentUser(): Promise<ApiResponse<User>> {
    return this.request('GET', '/auth/me');
  }

  // User Endpoints
  async getUser(id: number): Promise<ApiResponse<User>> {
    return this.request('GET', `/users/${id}`);
  }

  async updateUser(id: number, userData: Partial<User>): Promise<ApiResponse<User>> {
    return this.request('PUT', `/users/${id}`, userData);
  }

  async getUserPreferences(id: number): Promise<ApiResponse<any>> {
    return this.request('GET', `/users/${id}/preferences`);
  }

  async updateUserPreferences(id: number, preferences: any): Promise<ApiResponse<any>> {
    return this.request('PUT', `/users/${id}/preferences`, preferences);
  }

  async getUserStats(id: number): Promise<ApiResponse<any>> {
    return this.request('GET', `/users/${id}/stats`);
  }

  // Track Endpoints
  async getTracks(page = 0, size = 20, sort = 'name'): Promise<ApiResponse<PaginatedResponse<Track>>> {
    return this.request('GET', '/music/tracks', null, { page, size, sort });
  }

  async getTrack(id: number): Promise<ApiResponse<Track>> {
    return this.request('GET', `/music/tracks/${id}`);
  }

  async searchTracks(filters: SearchFilters): Promise<ApiResponse<SearchResult>> {
    return this.request('GET', '/music/search', null, filters);
  }

  async getPopularTracks(limit = 20): Promise<ApiResponse<Track[]>> {
    return this.request('GET', '/music/tracks/popular', null, { limit });
  }

  async getTrendingTracks(limit = 20): Promise<ApiResponse<Track[]>> {
    return this.request('GET', '/music/tracks/trending', null, { limit });
  }

  async getTracksByGenre(genre: string, limit = 20): Promise<ApiResponse<Track[]>> {
    return this.request('GET', `/music/tracks/genre/${genre}`, null, { limit });
  }

  // User-Track Interaction Endpoints
  async rateTrack(trackId: number, rating: number): Promise<ApiResponse<UserTrack>> {
    return this.request('POST', `/music/tracks/${trackId}/rate`, { rating });
  }

  async favoriteTrack(trackId: number): Promise<ApiResponse<UserTrack>> {
    return this.request('POST', `/music/tracks/${trackId}/favorite`);
  }

  async unfavoriteTrack(trackId: number): Promise<ApiResponse<void>> {
    return this.request('DELETE', `/music/tracks/${trackId}/favorite`);
  }

  async getUserTracks(userId: number, page = 0, size = 20): Promise<ApiResponse<PaginatedResponse<UserTrack>>> {
    return this.request('GET', `/users/${userId}/tracks`, null, { page, size });
  }

  async getUserFavorites(userId: number): Promise<ApiResponse<Track[]>> {
    return this.request('GET', `/users/${userId}/favorites`);
  }

  async getUserRecentlyPlayed(userId: number, limit = 20): Promise<ApiResponse<Track[]>> {
    return this.request('GET', `/users/${userId}/recently-played`, null, { limit });
  }

  // Playlist Endpoints
  async getPlaylists(page = 0, size = 20): Promise<ApiResponse<PaginatedResponse<Playlist>>> {
    return this.request('GET', '/playlists', null, { page, size });
  }

  async getPlaylist(id: number): Promise<ApiResponse<Playlist>> {
    return this.request('GET', `/playlists/${id}`);
  }

  async getUserPlaylists(userId: number): Promise<ApiResponse<Playlist[]>> {
    return this.request('GET', `/users/${userId}/playlists`);
  }

  async createPlaylist(playlistData: Partial<Playlist>): Promise<ApiResponse<Playlist>> {
    return this.request('POST', '/playlists', playlistData);
  }

  async updatePlaylist(id: number, playlistData: Partial<Playlist>): Promise<ApiResponse<Playlist>> {
    return this.request('PUT', `/playlists/${id}`, playlistData);
  }

  async deletePlaylist(id: number): Promise<ApiResponse<void>> {
    return this.request('DELETE', `/playlists/${id}`);
  }

  async addTrackToPlaylist(playlistId: number, trackId: number): Promise<ApiResponse<void>> {
    return this.request('POST', `/playlists/${playlistId}/tracks`, { trackId });
  }

  async removeTrackFromPlaylist(playlistId: number, trackId: number): Promise<ApiResponse<void>> {
    return this.request('DELETE', `/playlists/${playlistId}/tracks/${trackId}`);
  }

  async getPlaylistTracks(playlistId: number): Promise<ApiResponse<Track[]>> {
    return this.request('GET', `/playlists/${playlistId}/tracks`);
  }

  // Recommendation Endpoints
  async getRecommendations(request: RecommendationRequest): Promise<ApiResponse<RecommendationResponse>> {
    return this.request('POST', '/recommendations', request);
  }

  async getPersonalizedRecommendations(userId: number, limit = 20): Promise<ApiResponse<Track[]>> {
    return this.request('GET', `/recommendations/users/${userId}`, null, { limit });
  }

  async getSimilarTracks(trackId: number, limit = 10): Promise<ApiResponse<Track[]>> {
    return this.request('GET', `/recommendations/tracks/${trackId}/similar`, null, { limit });
  }

  async getDiscoveryPlaylist(userId: number): Promise<ApiResponse<Playlist>> {
    return this.request('GET', `/recommendations/users/${userId}/discovery-playlist`);
  }

  async refreshDiscoveryPlaylist(userId: number): Promise<ApiResponse<Playlist>> {
    return this.request('POST', `/recommendations/users/${userId}/discovery-playlist/refresh`);
  }

  // Spotify Integration Endpoints
  async syncWithSpotify(): Promise<ApiResponse<void>> {
    return this.request('POST', '/spotify/sync');
  }

  async importSpotifyPlaylists(): Promise<ApiResponse<Playlist[]>> {
    return this.request('POST', '/spotify/import/playlists');
  }

  async exportToSpotify(playlistId: number): Promise<ApiResponse<{ spotifyUrl: string }>> {
    return this.request('POST', `/spotify/export/playlist/${playlistId}`);
  }

  // Analytics Endpoints
  async getListeningStats(userId: number, period = '30d'): Promise<ApiResponse<any>> {
    return this.request('GET', `/analytics/users/${userId}/listening`, null, { period });
  }

  async getGenreStats(userId: number): Promise<ApiResponse<any>> {
    return this.request('GET', `/analytics/users/${userId}/genres`);
  }

  async getActivityStats(userId: number): Promise<ApiResponse<any>> {
    return this.request('GET', `/analytics/users/${userId}/activity`);
  }
}

// Create and export a singleton instance
const apiService = new ApiService();
export default apiService;
