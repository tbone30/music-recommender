import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { Track } from '../types';

interface PlayerState {
  currentTrack: Track | null;
  isPlaying: boolean;
  volume: number;
  position: number;
  duration: number;
  queue: Track[];
  repeatMode: 'off' | 'one' | 'all';
  shuffleMode: boolean;
}

interface PlayerContextType {
  playerState: PlayerState;
  playTrack: (track: Track) => void;
  pauseTrack: () => void;
  resumeTrack: () => void;
  nextTrack: () => void;
  previousTrack: () => void;
  setVolume: (volume: number) => void;
  seekTo: (position: number) => void;
  addToQueue: (track: Track) => void;
  removeFromQueue: (trackId: number) => void;
  clearQueue: () => void;
  setRepeatMode: (mode: 'off' | 'one' | 'all') => void;
  toggleShuffle: () => void;
}

const initialPlayerState: PlayerState = {
  currentTrack: null,
  isPlaying: false,
  volume: 0.7,
  position: 0,
  duration: 0,
  queue: [],
  repeatMode: 'off',
  shuffleMode: false,
};

const PlayerContext = createContext<PlayerContextType | undefined>(undefined);

interface PlayerProviderProps {
  children: ReactNode;
}

export const PlayerProvider: React.FC<PlayerProviderProps> = ({ children }) => {
  const [playerState, setPlayerState] = useState<PlayerState>(initialPlayerState);

  // Load saved player state from localStorage
  useEffect(() => {
    const savedVolume = localStorage.getItem('player_volume');
    const savedRepeatMode = localStorage.getItem('player_repeat_mode') as 'off' | 'one' | 'all';
    const savedShuffleMode = localStorage.getItem('player_shuffle_mode') === 'true';

    setPlayerState(prev => ({
      ...prev,
      volume: savedVolume ? parseFloat(savedVolume) : 0.7,
      repeatMode: savedRepeatMode || 'off',
      shuffleMode: savedShuffleMode,
    }));
  }, []);

  // Save settings to localStorage when they change
  useEffect(() => {
    localStorage.setItem('player_volume', playerState.volume.toString());
    localStorage.setItem('player_repeat_mode', playerState.repeatMode);
    localStorage.setItem('player_shuffle_mode', playerState.shuffleMode.toString());
  }, [playerState.volume, playerState.repeatMode, playerState.shuffleMode]);

  const playTrack = (track: Track) => {
    setPlayerState(prev => ({
      ...prev,
      currentTrack: track,
      isPlaying: true,
      position: 0,
    }));

    // Add to queue if not already there
    setPlayerState(prev => {
      const isInQueue = prev.queue.some(queueTrack => queueTrack.id === track.id);
      if (!isInQueue) {
        return {
          ...prev,
          queue: [...prev.queue, track],
        };
      }
      return prev;
    });
  };

  const pauseTrack = () => {
    setPlayerState(prev => ({
      ...prev,
      isPlaying: false,
    }));
  };

  const resumeTrack = () => {
    setPlayerState(prev => ({
      ...prev,
      isPlaying: true,
    }));
  };

  const nextTrack = () => {
    setPlayerState(prev => {
      const currentIndex = prev.queue.findIndex(track => 
        track.id === prev.currentTrack?.id
      );
      
      let nextIndex;
      if (prev.shuffleMode) {
        // Random next track
        nextIndex = Math.floor(Math.random() * prev.queue.length);
      } else {
        nextIndex = currentIndex + 1;
        if (nextIndex >= prev.queue.length) {
          if (prev.repeatMode === 'all') {
            nextIndex = 0;
          } else {
            return prev; // No next track
          }
        }
      }

      const nextTrack = prev.queue[nextIndex];
      return {
        ...prev,
        currentTrack: nextTrack,
        position: 0,
        isPlaying: true,
      };
    });
  };

  const previousTrack = () => {
    setPlayerState(prev => {
      const currentIndex = prev.queue.findIndex(track => 
        track.id === prev.currentTrack?.id
      );
      
      if (currentIndex > 0) {
        const previousTrack = prev.queue[currentIndex - 1];
        return {
          ...prev,
          currentTrack: previousTrack,
          position: 0,
          isPlaying: true,
        };
      }
      
      return prev;
    });
  };

  const setVolume = (volume: number) => {
    setPlayerState(prev => ({
      ...prev,
      volume: Math.max(0, Math.min(1, volume)),
    }));
  };

  const seekTo = (position: number) => {
    setPlayerState(prev => ({
      ...prev,
      position: Math.max(0, Math.min(prev.duration, position)),
    }));
  };

  const addToQueue = (track: Track) => {
    setPlayerState(prev => {
      const isInQueue = prev.queue.some(queueTrack => queueTrack.id === track.id);
      if (!isInQueue) {
        return {
          ...prev,
          queue: [...prev.queue, track],
        };
      }
      return prev;
    });
  };

  const removeFromQueue = (trackId: number) => {
    setPlayerState(prev => ({
      ...prev,
      queue: prev.queue.filter(track => track.id !== trackId),
    }));
  };

  const clearQueue = () => {
    setPlayerState(prev => ({
      ...prev,
      queue: [],
    }));
  };

  const setRepeatMode = (mode: 'off' | 'one' | 'all') => {
    setPlayerState(prev => ({
      ...prev,
      repeatMode: mode,
    }));
  };

  const toggleShuffle = () => {
    setPlayerState(prev => ({
      ...prev,
      shuffleMode: !prev.shuffleMode,
    }));
  };

  const value: PlayerContextType = {
    playerState,
    playTrack,
    pauseTrack,
    resumeTrack,
    nextTrack,
    previousTrack,
    setVolume,
    seekTo,
    addToQueue,
    removeFromQueue,
    clearQueue,
    setRepeatMode,
    toggleShuffle,
  };

  return (
    <PlayerContext.Provider value={value}>
      {children}
    </PlayerContext.Provider>
  );
};

export const usePlayer = (): PlayerContextType => {
  const context = useContext(PlayerContext);
  if (context === undefined) {
    throw new Error('usePlayer must be used within a PlayerProvider');
  }
  return context;
};
