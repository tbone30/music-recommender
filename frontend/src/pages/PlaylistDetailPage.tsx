import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import ApiService from '../services/api';
import { PlaylistDTO } from '../types/dto';

const PlaylistDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [playlist, setPlaylist] = useState<PlaylistDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const api = new ApiService();

  useEffect(() => {
    const fetchPlaylist = async () => {
      try {
        const data = await api.getPlaylist(id!);
        setPlaylist(data);
      } catch (err: any) {
        setError('Failed to load playlist.');
      } finally {
        setLoading(false);
      }
    };  
    if (id) fetchPlaylist();
  }, [id]);

  useEffect(() => {
      if (!loading && !error && playlist) {
        console.log(id);
        console.log(playlist);
      }
    }, [loading, error, playlist]);

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8">
        <h1 className="text-3xl font-bold text-white mb-6">Playlist Details</h1>
        <p className="text-gray-400">Loading...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="container mx-auto px-4 py-8">
        <h1 className="text-3xl font-bold text-white mb-6">Playlist Details</h1>
        <p className="text-red-400">{error}</p>
      </div>
    );
  }

  if (!playlist) {
    return (
      <div className="container mx-auto px-4 py-8">
        <h1 className="text-3xl font-bold text-white mb-6">Playlist Details</h1>
        <p className="text-gray-400">No playlist found.</p>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold text-white mb-6">{playlist.name}</h1>
      <div className="mb-4 flex items-center">
        {playlist.images && playlist.images[0] && (
          <img src={playlist.images[0].url} alt={playlist.name} className="w-32 h-32 object-cover rounded mr-6" />
        )}
        <div>
          <p className="text-gray-400 mb-2">{playlist.description}</p>
          <p className="text-gray-400 mb-2">Owner: {playlist.ownerDisplayName}</p>
          <p className="text-gray-400 mb-2">Public: {playlist.isPublic ? 'Yes' : 'No'}</p>
          <p className="text-gray-400 mb-2">Collaborative: {playlist.collaborative ? 'Yes' : 'No'}</p>
        </div>
      </div>
      <h2 className="text-2xl font-semibold text-white mb-4">Tracks</h2>
      {playlist.tracks && playlist.tracks.length > 0 ? (
        <ul className="divide-y divide-gray-700">
          {playlist.tracks.map((track) => (
            <li key={track.id} className="py-4">
              <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between">
                <div>
                  <span className="text-white font-medium">{track.name}</span>
                  {track.explicit && <span className="ml-2 text-xs text-red-400">Explicit</span>}
                  <span className="ml-4 text-gray-400">Duration: {track.duration}s</span>
                  <span className="ml-4 text-gray-400">Popularity: {track.popularity}</span>
                </div>
                <div className="mt-2 sm:mt-0">
                  <span className="text-gray-400">Artists: {track.artists.map(a => a.name).join(', ')}</span>
                </div>
              </div>
            </li>
          ))}
        </ul>
      ) : (
        <p className="text-gray-400">No tracks found in this playlist.</p>
      )}
    </div>
  );
};

export default PlaylistDetailPage;
