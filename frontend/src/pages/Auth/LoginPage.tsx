import React, { useState } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import LoadingSpinner from '../../components/UI/LoadingSpinner';

const LoginPage: React.FC = () => {
  const { login } = useAuth();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSpotifyLogin = async () => {
    try {
      setIsLoading(true);
      setError(null);
      await login();
    } catch (err) {
      console.error('Login failed:', err);
      setError('Failed to initiate Spotify login. Please try again.');
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 to-gray-800 flex items-center justify-center">
      <div className="max-w-md w-full space-y-8 p-8">
        <div className="text-center">
          <h2 className="text-4xl font-bold text-white mb-2">
            Welcome Back
          </h2>
          <p className="text-gray-300">
            Sign in to access your personalized music discovery experience
          </p>
        </div>

        <div className="bg-gray-800 rounded-lg p-8 shadow-lg">
          {error && (
            <div className="mb-6 p-4 bg-red-600 bg-opacity-10 border border-red-600 rounded-lg">
              <p className="text-red-400 text-sm">{error}</p>
            </div>
          )}
          
          <div className="space-y-6">
            <button
              onClick={handleSpotifyLogin}
              disabled={isLoading}
              className="w-full bg-green-600 text-white py-3 px-4 rounded-lg font-semibold hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed transition duration-300 flex items-center justify-center space-x-2"
            >
              {isLoading ? (
                <>
                  <LoadingSpinner />
                  <span>Connecting...</span>
                </>
              ) : (
                <>
                  <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M12 2C6.477 2 2 6.477 2 12s4.477 10 10 10 10-4.477 10-10S17.523 2 12 2zm4.586 14.424c-.18.295-.563.387-.857.207-2.35-1.434-5.305-1.76-8.786-.963-.335.077-.67-.133-.746-.47-.077-.334.132-.67.47-.745 3.808-.871 7.077-.496 9.713 1.115.293.18.386.563.206.856zm1.223-2.723c-.226.367-.706.482-1.072.257-2.687-1.652-6.785-2.131-9.965-1.166-.413.125-.849-.106-.973-.517-.125-.413.106-.849.517-.973 3.632-1.102 8.147-.568 11.234 1.328.366.226.481.707.259 1.071zm.105-2.835C14.692 8.95 9.375 8.775 6.297 9.71c-.493.15-1.016-.128-1.166-.62-.149-.493.129-1.016.622-1.165 3.532-1.073 9.404-.865 13.115 1.338.445.264.590.837.327 1.282-.264.444-.838.590-1.282.326z"/>
                  </svg>
                  <span>Continue with Spotify</span>
                </>
              )}
            </button>

            <div className="text-center">
              <p className="text-sm text-gray-400">
                By continuing, you agree to our Terms of Service and Privacy Policy.
                We'll access your Spotify data to provide personalized recommendations.
              </p>
            </div>
          </div>
        </div>

        <div className="text-center">
          <p className="text-gray-400 text-sm">
            Don't have a Spotify account?{' '}
            <a 
              href="https://www.spotify.com/us/signup?forward_url=https%3A%2F%2Fopen.spotify.com%2F" 
              target="_blank" 
              rel="noopener noreferrer"
              className="text-green-400 hover:text-green-300 font-medium"
            >
              Sign up for free
            </a>
          </p>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
