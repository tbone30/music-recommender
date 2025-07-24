import React, { useEffect, useState } from 'react';
import ApiService from '../services/api';
import { PlaylistListPageDTO, SimplifiedPlaylistDTO } from './playlistTypes';


const PlaylistsPage: React.FC = () => {
  const [playlists, setPlaylists] = useState<SimplifiedPlaylistDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [api, setApi] = useState<ApiService>(new ApiService());

  useEffect(() => {
    const fetchPlaylists = async () => {
      try {
        const data: any = await api.getUserPlaylists();
        console.log(data);
        setPlaylists(data.items || []);
      } catch (err: any) {
        setError('Failed to load playlists.');
      } finally {
        setLoading(false);
      }
    };
    fetchPlaylists();
  }, []);

  // Log playlists to console if not loading, not error, and playlists is empty
  useEffect(() => {
    if (!loading && !error && playlists.length === 0) {
      console.log(playlists);
    }
  }, [loading, error, playlists]);

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-white mb-6">Your Playlists</h1>
      {loading && <p className="text-gray-400">Loading playlists...</p>}
      {error && <p className="text-red-400">{error}</p>}
      {!loading && !error && playlists.length === 0 && (
        <p className="text-gray-400">No playlists found.</p>
      )}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
        {playlists.map((playlist) => (
          <div key={playlist.id} className="bg-gray-800 rounded-lg p-4 flex flex-col items-center">
            {playlist.images && playlist.images[0] && (
              <img src={playlist.images[0].url} alt={playlist.name} className="w-32 h-32 object-cover rounded mb-4" />
            )}
            <h2 className="text-xl font-semibold text-white mb-2 text-center">{playlist.name}</h2>
            {playlist.description && (
              <p className="text-gray-400 text-sm text-center">{playlist.description}</p>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default PlaylistsPage;
