import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import ApiService from '../services/api';
import { PlaylistListPageDTO, SimplifiedPlaylistDTO } from '../types/dto';


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
    <div className="w-full min-h-[80vh] px-2 sm:px-4 md:px-8 py-8 md:py-12 bg-gradient-to-br from-[#1db954]/20 to-[#191414]">
      <div className="max-w-6xl mx-auto">
        <h1 className="text-4xl font-extrabold text-white mb-8 tracking-tight drop-shadow-lg text-center">Your Playlists</h1>
        {loading && <p className="text-gray-400 text-center text-lg">Loading playlists...</p>}
        {error && <p className="text-red-400 text-center text-lg">{error}</p>}
        {!loading && !error && playlists.length === 0 && (
          <p className="text-gray-400 text-center text-lg">No playlists found.</p>
        )}
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8 mt-6">
          {playlists.map((playlist) => (
            <Link
              key={playlist.id}
              to={`/playlists/${playlist.id}`}
              className="group bg-gradient-to-br from-[#232323] to-[#191414] rounded-2xl p-6 flex flex-col items-center shadow-xl hover:scale-[1.03] hover:shadow-2xl hover:from-[#1db954]/40 hover:to-[#232323] transition-all duration-200 cursor-pointer border border-[#232323] focus:outline-none focus:ring-2 focus:ring-[#1db954]"
              style={{ textDecoration: 'none' }}
            >
              {playlist.images && playlist.images[0] ? (
                <img src={playlist.images[0].url} alt={playlist.name} className="w-28 h-28 md:w-32 md:h-32 object-cover rounded-lg shadow-lg border-2 border-[#282828] mb-4" />
              ) : (
                <div className="w-28 h-28 md:w-32 md:h-32 bg-[#282828] rounded-lg mb-4 flex items-center justify-center">
                  <svg className="w-12 h-12 text-[#1db954]" fill="currentColor" viewBox="0 0 24 24"><path d="M12 2a10 10 0 100 20 10 10 0 000-20zm4.93 7.36c-.21-.33-.65-.43-.98-.22-2.69 1.67-6.09 2.05-10.09 1.13-.39-.09-.78.15-.87.54-.09.39.15.78.54.87 4.32.99 7.99.57 10.93-1.23.33-.21.43-.65.22-.98zm1.12 2.95c-.26-.36-.76-.45-1.12-.19-2.3 1.65-5.8 2.13-9.54 1.13-.41-.11-.84.13-.95.54-.11.41.13.84.54.95 4.09 1.09 7.93.56 10.5-1.25.36-.26.45-.76.19-1.12zm-1.13 2.97c-.29-.39-.85-.48-1.24-.19-1.97 1.47-5.01 1.6-8.19.7-.44-.12-.9.13-1.02.57-.12.44.13.9.57 1.02 3.51.97 6.93.82 9.18-.77.39-.29.48-.85.19-1.24z"/></svg>
                </div>
              )}
              <h2 className="text-lg md:text-xl font-bold text-white mb-2 text-center w-full truncate group-hover:text-[#1db954] transition-colors">{playlist.name}</h2>
              {playlist.description && (
                <p className="text-gray-400 text-xs md:text-sm text-center line-clamp-2 w-full">{playlist.description}</p>
              )}
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
};

export default PlaylistsPage;
