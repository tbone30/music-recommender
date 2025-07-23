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

  //remove below


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
}

// Create and export a singleton instance
const apiService = new ApiService();
export default apiService;
