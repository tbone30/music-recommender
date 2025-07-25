import React from 'react';
import { useAuth } from '../../contexts/AuthContext';

interface LayoutProps {
  children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const { isAuthenticated } = useAuth();

  return (
    <div className="flex flex-col min-h-screen bg-gray-900 text-white">
      {/* Header */}
      <header className="bg-gradient-to-r from-[#232323] via-[#191414] to-[#232323] shadow-lg rounded-tr-2xl border-b border-[#232323]">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-20 md:h-24">
            <div className="flex items-center gap-3">
              <svg className="w-8 h-8 md:w-10 md:h-10 text-[#1db954]" fill="currentColor" viewBox="0 0 24 24"><path d="M12 2a10 10 0 100 20 10 10 0 000-20zm4.93 7.36c-.21-.33-.65-.43-.98-.22-2.69 1.67-6.09 2.05-10.09 1.13-.39-.09-.78.15-.87.54-.09.39.15.78.54.87 4.32.99 7.99.57 10.93-1.23.33-.21.43-.65.22-.98zm1.12 2.95c-.26-.36-.76-.45-1.12-.19-2.3 1.65-5.8 2.13-9.54 1.13-.41-.11-.84.13-.95.54-.11.41.13.84.54.95 4.09 1.09 7.93.56 10.5-1.25.36-.26.45-.76.19-1.12zm-1.13 2.97c-.29-.39-.85-.48-1.24-.19-1.97 1.47-5.01 1.6-8.19.7-.44-.12-.9.13-1.02.57-.12.44.13.9.57 1.02 3.51.97 6.93.82 9.18-.77.39-.29.48-.85.19-1.24z"/></svg>
              <h1 className="text-2xl md:text-3xl font-extrabold text-white tracking-tight drop-shadow-lg select-none">
                <span className="text-[#1db954]">Spotify</span> Discovery
              </h1>
            </div>
            
            {isAuthenticated && (
              <nav className="hidden md:block">
                <div className="ml-10 flex items-baseline space-x-2 md:space-x-4">
                  <a
                    href="/dashboard"
                    className="text-gray-200 hover:text-[#1db954] px-3 py-2 rounded-md text-base font-semibold transition-colors duration-150"
                  >
                    Dashboard
                  </a>
                  <a
                    href="/discover"
                    className="text-gray-200 hover:text-[#1db954] px-3 py-2 rounded-md text-base font-semibold transition-colors duration-150"
                  >
                    Discover
                  </a>
                  <a
                    href="/playlists"
                    className="text-gray-200 hover:text-[#1db954] px-3 py-2 rounded-md text-base font-semibold transition-colors duration-150"
                  >
                    Playlists
                  </a>
                  <a
                    href="/profile"
                    className="text-gray-200 hover:text-[#1db954] px-3 py-2 rounded-md text-base font-semibold transition-colors duration-150"
                  >
                    Profile
                  </a>
                  <a
                    href="/settings"
                    className="text-gray-200 hover:text-[#1db954] px-3 py-2 rounded-md text-base font-semibold transition-colors duration-150"
                  >
                    Settings
                  </a>
                </div>
              </nav>
            )}
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="flex-1 flex flex-col justify-stretch">
        {children}
      </main>

      {/* Footer */}
      <footer className="bg-gray-800 border-t border-gray-700">
        <div className="max-w-7xl mx-auto py-4 px-4 sm:px-6 lg:px-8">
          <p className="text-center text-gray-400 text-sm">
            © 2025 Spotify Discovery Platform. All rights reserved.
          </p>
        </div>
      </footer>
    </div>
  );
};

export default Layout;
