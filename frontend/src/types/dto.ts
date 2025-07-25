export interface SimplifiedPlaylistDTO {
  id: string;
  name: string;
  description?: string;
  images?: { url: string }[];
  [key: string]: any;
}

export interface PlaylistListPageDTO {
  href: string;
  limit: number;
  next: string | null;
  offset: number;
  previous: string | null;
  total: number;
  items: SimplifiedPlaylistDTO[];
}

export interface ArtistDTO {
  id: string;
  name: string;
  popularity: number;
  genres: string[];
  images: { url: string }[];
  href: string;
  uri: string;
}

export interface TrackDTO {
  id: string;
  name: string;
  duration: number;
  explicit: boolean;
  popularity: number;
  artists: ArtistDTO[];
  href: string;
  uri: string;
  albumId: string;
  albumImages: { url: string }[];
  albumName: string;
}

export interface PlaylistDTO {
  collaborative: boolean;
  description: string;
  href: string;
  id: string;
  images: { url: string }[];
  name: string;
  ownerId: string;
  ownerDisplayName: string;
  isPublic: boolean;
  tracks: TrackDTO[];
  uri: string;
}

export interface AlbumDTO {
  id: string;
  name: string;
  totalTracks: number;
  artists: ArtistDTO[];
  tracks: TrackDTO[];
  popularity: number;
  href: string;
  releaseDate: string;
  releaseDatePrecision: string;
  images: { url: string }[];
  albumType: 'album' | 'single' | 'compilation';
  uri: string;
}

export interface SimplifiedAlbumDTO {
  albumType: 'album' | 'single' | 'compilation';
  totalTracks: number;
  href: string;
  id: string;
  images: { url: string }[];
  name: string;
  releaseDate: string;
  releaseDatePrecision: string;
  uri: string;
  artists: SimplifiedArtistDTO[];
}

export interface SimplifiedArtistDTO {
  href: string;
  id: string;
  name: string;
  uri: string;
}