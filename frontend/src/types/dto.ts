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
  images: { url: string }[];
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