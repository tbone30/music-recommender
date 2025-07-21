import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { User } from '../types';
import SpotifyAuthService, { SpotifyUser } from '../services/spotifyAuthService';

interface AuthContextType {
  user: SpotifyUser | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: () => Promise<void>;
  logout: () => void;
  refreshUser: () => Promise<void>;
  spotifyAuthService: SpotifyAuthService;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<SpotifyUser | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [spotifyAuthService] = useState(() => new SpotifyAuthService());

  const isAuthenticated = !!user;

  useEffect(() => {
    checkAuthStatus();
  }, []);

  const checkAuthStatus = async () => {
    try {
      setIsLoading(true);
      if (spotifyAuthService.isConnected()) {
        const isValid = await spotifyAuthService.validateToken();
        if (isValid) {
          const userProfile = await spotifyAuthService.getUserProfile();
          setUser(userProfile);
        } else {
          spotifyAuthService.disconnect();
        }
      }
    } catch (error) {
      console.error('Auth check failed:', error);
      spotifyAuthService.disconnect();
    } finally {
      setIsLoading(false);
    }
  };

  const login = async (): Promise<void> => {
    try {
      await spotifyAuthService.initiateSpotifyAuth();
    } catch (error) {
      console.error('Login failed:', error);
      throw error;
    }
  };

  const logout = async () => {
    try {
      spotifyAuthService.disconnect();
      setUser(null);
      window.location.href = '/';
    } catch (error) {
      console.error('Logout error:', error);
    }
  };

  const refreshUser = async () => {
    try {
      if (spotifyAuthService.isConnected()) {
        const userProfile = await spotifyAuthService.getUserProfile();
        setUser(userProfile);
      } else {
        setUser(null);
      }
    } catch (error) {
      console.error('Failed to refresh user:', error);
      logout();
    }
  };

  const value: AuthContextType = {
    user,
    isAuthenticated,
    isLoading,
    login,
    logout,
    refreshUser,
    spotifyAuthService,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
