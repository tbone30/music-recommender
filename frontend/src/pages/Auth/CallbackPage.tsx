import React, { useEffect, useState, useRef } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import LoadingSpinner from '../../components/UI/LoadingSpinner';

const CallbackPage: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { refreshUser, spotifyAuthService } = useAuth();
  const [status, setStatus] = useState<'loading' | 'success' | 'error'>('loading');
  const [error, setError] = useState<string>('');
  const hasProcessedCallback = useRef(false);

  useEffect(() => {
    const handleCallback = async () => {
      // Prevent multiple executions
      if (hasProcessedCallback.current) {
        return;
      }
      hasProcessedCallback.current = true;

      try {
        const code = searchParams.get('code');
        const state = searchParams.get('state');
        const errorParam = searchParams.get('error');

        if (errorParam) {
          throw new Error(`Spotify authorization failed: ${errorParam}`);
        }

        if (!code) {
          throw new Error('Missing authorization code');
        }

        // Exchange code for tokens using our new auth service
        await spotifyAuthService.handleCallback(code, state || undefined);
        
        // Refresh user data
        await refreshUser();
        
        setStatus('success');
        
        // Redirect to dashboard after a brief success message
        setTimeout(() => {
          navigate('/dashboard');
        }, 2000);

      } catch (err) {
        console.error('Callback handling failed:', err);
        setError(err instanceof Error ? err.message : 'Unknown error occurred');
        setStatus('error');
        
        // Redirect to login after showing error
        setTimeout(() => {
          navigate('/login');
        }, 3000);
      }
    };

    handleCallback();
  }, [searchParams, navigate, refreshUser, spotifyAuthService]);

  if (status === 'loading') {
    return (
      <div className="min-h-screen bg-gradient-to-br from-gray-900 to-gray-800 flex items-center justify-center">
        <div className="text-center">
          <LoadingSpinner />
          <h2 className="text-2xl font-bold text-white mt-4">
            Connecting to Spotify...
          </h2>
          <p className="text-gray-300 mt-2">
            Please wait while we set up your account
          </p>
        </div>
      </div>
    );
  }

  if (status === 'success') {
    return (
      <div className="min-h-screen bg-gradient-to-br from-gray-900 to-gray-800 flex items-center justify-center">
        <div className="text-center">
          <div className="w-16 h-16 bg-green-600 rounded-full flex items-center justify-center mx-auto mb-4">
            <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
            </svg>
          </div>
          <h2 className="text-2xl font-bold text-white">
            Successfully Connected!
          </h2>
          <p className="text-gray-300 mt-2">
            Redirecting to your dashboard...
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 to-gray-800 flex items-center justify-center">
      <div className="text-center max-w-md mx-auto p-8">
        <div className="w-16 h-16 bg-red-600 rounded-full flex items-center justify-center mx-auto mb-4">
          <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
          </svg>
        </div>
        <h2 className="text-2xl font-bold text-white">
          Connection Failed
        </h2>
        <p className="text-gray-300 mt-2 mb-4">
          {error || 'Unable to connect to Spotify'}
        </p>
        <p className="text-gray-400 text-sm">
          Redirecting to login page...
        </p>
      </div>
    </div>
  );
};

export default CallbackPage;
