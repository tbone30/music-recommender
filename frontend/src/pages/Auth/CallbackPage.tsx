import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import LoadingSpinner from '../../components/UI/LoadingSpinner';
import apiService from '../../services/api';

const CallbackPage: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { refreshUser } = useAuth();

  useEffect(() => {
    const handleCallback = async () => {
      try {
        const code = searchParams.get('code');
        const error = searchParams.get('error');
        const redirectTo = searchParams.get('state');

        if (error) {
          console.error('Spotify auth error:', error);
          navigate('/login?error=access_denied');
          return;
        }

        if (!code) {
          console.error('No authorization code received');
          navigate('/login?error=no_code');
          return;
        }

        // Exchange code for token
        const response = await apiService.spotifyAuth(code);
        
        if (response.success && response.data.token) {
          // Store token
          localStorage.setItem('spotify_discovery_token', response.data.token);
          
          // Refresh user data
          await refreshUser();

          // Redirect to intended page or dashboard
          const destination = redirectTo || '/dashboard';
          navigate(destination, { replace: true });
        } else {
          throw new Error('Failed to authenticate with Spotify');
        }
      } catch (error) {
        console.error('Callback error:', error);
        navigate('/login?error=auth_failed');
      }
    };

    handleCallback();
  }, [searchParams, navigate, refreshUser]);

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 to-gray-800 flex items-center justify-center">
      <div className="text-center">
        <LoadingSpinner size="lg" />
        <h2 className="text-2xl font-semibold text-white mt-4">
          Connecting to Spotify...
        </h2>
        <p className="text-gray-400 mt-2">
          Please wait while we set up your account.
        </p>
      </div>
    </div>
  );
};

export default CallbackPage;
