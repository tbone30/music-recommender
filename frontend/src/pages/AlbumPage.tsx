
import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import ApiService from '../services/api';
import { AlbumDTO, TrackDTO } from '../types/dto';

const AlbumPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [album, setAlbum] = useState<AlbumDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const api = new ApiService();

  useEffect(() => {
    const fetchAlbum = async () => {
      try {
        const data = await api.getPublicAlbum(id!);
        setAlbum(data);
      } catch (err: any) {
        setError('Failed to load album.');
      } finally {
        setLoading(false);
      }
    };
    if (id) fetchAlbum();
  }, [id]);

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] bg-gradient-to-br from-[#1db954]/30 to-[#191414] rounded-xl shadow-lg animate-pulse">
        <div className="w-24 h-24 bg-[#282828] rounded-full mb-6" />
        <h1 className="text-4xl font-extrabold text-white mb-4 tracking-tight">Loading Album...</h1>
        <p className="text-gray-400 text-lg">Please wait while we fetch the album.</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] bg-gradient-to-br from-[#191414] to-[#282828] rounded-xl shadow-lg">
        <h1 className="text-4xl font-extrabold text-white mb-4 tracking-tight">Album Details</h1>
        <p className="text-red-400 text-lg">{error}</p>
      </div>
    );
  }

  if (!album) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] bg-gradient-to-br from-[#191414] to-[#282828] rounded-xl shadow-lg">
        <h1 className="text-4xl font-extrabold text-white mb-4 tracking-tight">Album Details</h1>
        <p className="text-gray-400 text-lg">No album found.</p>
      </div>
    );
  }

  return (
    <div className="w-full px-2 sm:px-4 md:px-8 py-6 md:py-10 mx-auto">
      <div className="flex flex-col items-center w-full max-w-5xl mx-auto mb-8 md:mb-10">
        {album.images && album.images[0] && (
          <img
            src={album.images[0].url}
            alt={album.name}
            className="w-32 h-32 md:w-40 md:h-40 object-cover rounded-lg shadow-lg border-4 border-[#282828] mb-4 flex-shrink-0 mx-auto"
          />
        )}
        <h1 className="text-3xl md:text-5xl font-extrabold text-white mb-2 tracking-tight drop-shadow-lg break-words w-full text-center">{album.name}</h1>
        {Array.isArray(album.artists) && album.artists.length > 0 && (
          <div className="text-base md:text-lg text-gray-200 mb-2 w-full text-center">
            By {album.artists.map((artist, idx) => (
              <span key={artist.id}>
                <Link to={`/artists/${artist.id}`} className="hover:underline text-[#1db954]">{artist.name}</Link>{idx < album.artists.length - 1 ? ', ' : ''}
              </span>
            ))}
          </div>
        )}
        {album.releaseDate && (
          <div className="text-sm text-gray-400 mb-2">Released: {album.releaseDate}</div>
        )}
        {typeof album.totalTracks === 'number' && (
          <div className="text-xs text-gray-400 mb-2">{album.totalTracks} tracks</div>
        )}
      </div>
      <div className="bg-[#191414] rounded-xl shadow-lg p-4 md:p-6 w-full max-w-5xl mx-auto">
        <h2 className="text-xl md:text-2xl font-bold text-white mb-4 md:mb-6 flex items-center gap-2">
          <svg className="w-7 h-7 text-[#1db954]" fill="currentColor" viewBox="0 0 24 24"><path d="M12 2a10 10 0 100 20 10 10 0 000-20zm4.93 7.36c-.21-.33-.65-.43-.98-.22-2.69 1.67-6.09 2.05-10.09 1.13-.39-.09-.78.15-.87.54-.09.39.15.78.54.87 4.32.99 7.99.57 10.93-1.23.33-.21.43-.65.22-.98zm1.12 2.95c-.26-.36-.76-.45-1.12-.19-2.3 1.65-5.8 2.13-9.54 1.13-.41-.11-.84.13-.95.54-.11.41.13.84.54.95 4.09 1.09 7.93.56 10.5-1.25.36-.26.45-.76.19-1.12zm-1.13 2.97c-.29-.39-.85-.48-1.24-.19-1.97 1.47-5.01 1.6-8.19.7-.44-.12-.9.13-1.02.57-.12.44.13.9.57 1.02 3.51.97 6.93.82 9.18-.77.39-.29.48-.85.19-1.24z"/></svg>
          Tracks
        </h2>
        {album.tracks && album.tracks.length > 0 ? (
          <ul className="divide-y divide-[#282828]">
            {album.tracks.map((track, idx) => (
              <li key={track.id} className="py-3 md:py-4 flex items-center group hover:bg-[#232323] rounded-lg transition-colors px-1 md:px-2">
                <span className="w-6 md:w-8 text-gray-500 font-mono text-base md:text-lg mr-2 md:mr-4 text-center flex-shrink-0">{idx + 1}</span>
                {/* Album image */}
                {track.albumImages && track.albumImages[0] && track.albumImages[0].url ? (
                  <img
                    src={track.albumImages[0].url}
                    alt={track.albumName || 'Album cover'}
                    className="w-10 h-10 md:w-12 md:h-12 object-cover rounded shadow mr-3 md:mr-4 flex-shrink-0"
                  />
                ) : (
                  <div className="w-10 h-10 md:w-12 md:h-12 bg-[#282828] rounded mr-3 md:mr-4 flex-shrink-0" />
                )}
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-1 md:gap-2">
                    <span className="text-white font-semibold text-base md:text-lg truncate max-w-[10rem] md:max-w-xs">{track.name}</span>
                    {track.explicit && <span className="ml-2 text-xs text-red-400 bg-[#282828] px-2 py-0.5 rounded-full">Explicit</span>}
                  </div>
                  <div className="flex flex-wrap gap-1 md:gap-2 text-xs md:text-sm text-gray-400 mt-1 items-center">
                    <span>Artists: {track.artists.map(a => a.name).join(', ')}</span>
                  </div>
                </div>
              </li>
            ))}
          </ul>
        ) : (
          <p className="text-gray-400 text-center">No tracks found in this album.</p>
        )}
      </div>
    </div>
  );
};

export default AlbumPage;