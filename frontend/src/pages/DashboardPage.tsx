import React from 'react';
import { useAuth } from '../contexts/AuthContext';

const DashboardPage: React.FC = () => {
  const { user } = useAuth();

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-white mb-2">
          Welcome back, {user?.displayName || 'Music Lover'}!
        </h1>
        <p className="text-gray-400">
          Here's your personalized music discovery dashboard.
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {/* Quick Stats */}
        <div className="bg-gray-800 rounded-lg p-6">
          <h3 className="text-lg font-semibold text-white mb-2">Your Stats</h3>
          <div className="space-y-2">
            <div className="flex justify-between">
              <span className="text-gray-400">Favorite Tracks</span>
              <span className="text-white">--</span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-400">Playlists</span>
              <span className="text-white">--</span>
            </div>
            <div className="flex justify-between">
              <span className="text-gray-400">Listening Time</span>
              <span className="text-white">-- hrs</span>
            </div>
          </div>
        </div>

        {/* Recent Activity */}
        <div className="bg-gray-800 rounded-lg p-6">
          <h3 className="text-lg font-semibold text-white mb-2">Recent Activity</h3>
          <p className="text-gray-400">
            Your recent listening activity will appear here.
          </p>
        </div>

        {/* Recommendations */}
        <div className="bg-gray-800 rounded-lg p-6">
          <h3 className="text-lg font-semibold text-white mb-2">New Recommendations</h3>
          <p className="text-gray-400">
            Fresh music recommendations based on your taste.
          </p>
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;
