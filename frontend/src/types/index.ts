// Spotify Discovery Platform - Type Definitions

/**
 * User-related types
 */
export interface User {
  id: number;
  spotifyId: string;
  displayName: string;
  email: string;
  profileImageUrl?: string;
  country?: string;
  subscriptionType?: 'free' | 'premium';
  followersCount?: number;
  preferredGenres?: string;
  acousticnessPreference?: number;
  danceabilityPreference?: number;
  energyPreference?: number;
  valencePreference?: number;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

/**
 * Track-related types
 */
export interface Track {
  id: number;
  spotifyId: string;
  name: string;
  artistName: string;
  albumName?: string;
  albumImageUrl?: string;
  durationMs?: number;
  popularity?: number;
  previewUrl?: string;
  externalUrl?: string;
  isExplicit?: boolean;
  // Audio Features
  acousticness?: number;
  danceability?: number;
  energy?: number;
  instrumentalness?: number;
  liveness?: number;
  loudness?: number;
  speechiness?: number;
  valence?: number;
  tempo?: number;
  timeSignature?: number;
  key?: number;
  mode?: number;
  genres?: string;
  recommendationScore?: number;
  playCount: number;
  averageRating?: number;
  createdAt: string;
  updatedAt: string;
}

/**
 * Playlist-related types
 */
export interface Playlist {
  id: number;
  spotifyId?: string;
  name: string;
  description?: string;
  imageUrl?: string;
  isPublic: boolean;
  isCollaborative: boolean;
  trackCount: number;
  followerCount: number;
  isDiscoveryPlaylist: boolean;
  playlistType?: string;
  tags?: string;
  averageRating?: number;
  moodProfile?: string;
  isSyncedWithSpotify: boolean;
  lastSyncedAt?: string;
  user: User;
  createdAt: string;
  updatedAt: string;
}

/**
 * User-Track interaction types
 */
export interface UserTrack {
  id: number;
  user: User;
  track: Track;
  rating?: number;
  playCount: number;
  isFavorite: boolean;
  isDisliked: boolean;
  isSkipped: boolean;
  skipCount: number;
  completedPlays: number;
  listenTimePercentage?: number;
  lastPlayedAt?: string;
  firstPlayedAt?: string;
  playlistContext?: string;
  deviceType?: string;
  timeOfDay?: string;
  dayOfWeek?: string;
  createdAt: string;
  updatedAt: string;
}

/**
 * API Response types
 */
export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  errors?: string[];
  meta?: {
    page?: number;
    limit?: number;
    total?: number;
    totalPages?: number;
  };
}

export interface PaginatedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

/**
 * Authentication types
 */
export interface AuthState {
  isAuthenticated: boolean;
  user: User | null;
  token: string | null;
  isLoading: boolean;
  error: string | null;
}

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface SpotifyAuthResponse {
  access_token: string;
  token_type: string;
  expires_in: number;
  refresh_token: string;
  scope: string;
}

/**
 * Music recommendation types
 */
export interface RecommendationRequest {
  seedTracks?: string[];
  seedArtists?: string[];
  seedGenres?: string[];
  targetAcousticness?: number;
  targetDanceability?: number;
  targetEnergy?: number;
  targetValence?: number;
  targetTempo?: number;
  limit?: number;
}

export interface RecommendationResponse {
  tracks: Track[];
  seedInfo: {
    seedTracks: string[];
    seedArtists: string[];
    seedGenres: string[];
  };
  confidence: number;
  explanations?: string[];
}

/**
 * Search types
 */
export interface SearchFilters {
  query?: string;
  type?: 'track' | 'artist' | 'album' | 'playlist';
  genre?: string;
  minPopularity?: number;
  maxPopularity?: number;
  minTempo?: number;
  maxTempo?: number;
  minEnergy?: number;
  maxEnergy?: number;
  minDanceability?: number;
  maxDanceability?: number;
}

export interface SearchResult {
  tracks: Track[];
  artists: Artist[];
  albums: Album[];
  playlists: Playlist[];
  total: number;
}

/**
 * Additional music types
 */
export interface Artist {
  id: string;
  name: string;
  imageUrl?: string;
  genres?: string[];
  popularity?: number;
  followers?: number;
  externalUrl?: string;
}

export interface Album {
  id: string;
  name: string;
  artistName: string;
  imageUrl?: string;
  releaseDate?: string;
  totalTracks?: number;
  albumType?: 'album' | 'single' | 'compilation';
  externalUrl?: string;
}

/**
 * UI State types
 */
export interface LoadingState {
  isLoading: boolean;
  message?: string;
}

export interface ErrorState {
  hasError: boolean;
  message: string;
  code?: string;
}

export interface ToastNotification {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  title: string;
  message?: string;
  duration?: number;
}

/**
 * Music player types
 */
export interface PlayerState {
  currentTrack: Track | null;
  isPlaying: boolean;
  volume: number;
  position: number;
  duration: number;
  queue: Track[];
  shuffled: boolean;
  repeatMode: 'none' | 'track' | 'playlist';
}

/**
 * Dashboard/Analytics types
 */
export interface UserStats {
  totalTracks: number;
  totalPlaylists: number;
  totalListeningTime: number;
  favoriteGenres: string[];
  topArtists: Artist[];
  topTracks: Track[];
  listeningHistory: {
    date: string;
    playCount: number;
    uniqueTracks: number;
  }[];
}

/**
 * Settings types
 */
export interface UserPreferences {
  theme: 'light' | 'dark' | 'system';
  language: string;
  notifications: {
    newRecommendations: boolean;
    playlistUpdates: boolean;
    socialActivity: boolean;
  };
  privacy: {
    profileVisible: boolean;
    playlistsVisible: boolean;
    listeningActivityVisible: boolean;
  };
  audioQuality: 'auto' | 'high' | 'low';
}

/**
 * Form types
 */
export interface PlaylistFormData {
  name: string;
  description?: string;
  isPublic: boolean;
  isCollaborative: boolean;
}

export interface UserProfileFormData {
  displayName: string;
  email: string;
  country?: string;
  preferredGenres?: string[];
}

/**
 * Component prop types
 */
export interface ComponentProps {
  className?: string;
  children?: React.ReactNode;
}

export interface TrackItemProps extends ComponentProps {
  track: Track;
  showAlbum?: boolean;
  showArtist?: boolean;
  showDuration?: boolean;
  showActions?: boolean;
  onPlay?: (track: Track) => void;
  onAddToPlaylist?: (track: Track) => void;
  onLike?: (track: Track) => void;
}

export interface PlaylistItemProps extends ComponentProps {
  playlist: Playlist;
  showOwner?: boolean;
  showDescription?: boolean;
  onClick?: (playlist: Playlist) => void;
}
