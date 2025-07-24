// TypeScript interface for PlaylistListPageDTO and SimplifiedPlaylistDTO
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
