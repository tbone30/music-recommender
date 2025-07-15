# 🎵 Spotify Music Discovery Platform

A full-stack web application that provides personalized music recommendations and discovery features using Spotify's Web API. Built with React TypeScript frontend and Spring Boot backend, featuring machine learning-powered recommendations and comprehensive music analytics.

## 🚀 Features

### 🎯 Core Features
- **Spotify OAuth Integration** - Seamless authentication with Spotify accounts
- **Personalized Recommendations** - ML-powered music discovery based on listening habits
- **Smart Playlist Generation** - Automated playlist creation with mood and genre targeting
- **Music Analytics Dashboard** - Comprehensive insights into listening patterns
- **Social Discovery** - Find music through users with similar tastes
- **Cross-Platform Sync** - Sync playlists and preferences across devices

### 🎨 User Experience
- **Modern UI/UX** - Spotify-inspired dark theme with Tailwind CSS
- **Responsive Design** - Optimized for desktop, tablet, and mobile
- **Real-time Updates** - Live synchronization with Spotify data
- **Advanced Search** - Multi-criteria filtering and discovery tools
- **Music Player Integration** - Built-in player with queue management

### 🤖 Machine Learning
- **Audio Feature Analysis** - Acousticness, danceability, energy, valence analysis
- **Collaborative Filtering** - User-based and item-based recommendations
- **Content-Based Filtering** - Genre, artist, and audio feature matching
- **Hybrid Recommendations** - Combined ML approaches for better accuracy

## 🏗️ Technology Stack

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.2.0, Java 17
- **Security**: Spring Security with OAuth2
- **Database**: SQLite (development) → PostgreSQL (production)
- **API Integration**: Spotify Web API
- **Build Tool**: Maven
- **Testing**: JUnit 5, Spring Boot Test

### Frontend (React TypeScript)
- **Framework**: React 19.1.0 with TypeScript
- **Styling**: Tailwind CSS 3.3+
- **State Management**: React Query, Context API
- **Routing**: React Router v6
- **Charts**: Recharts for analytics
- **Icons**: Lucide React, Heroicons
- **Build Tool**: Create React App

### Infrastructure & Deployment
- **Frontend Hosting**: AWS S3 + CloudFront
- **Backend Hosting**: AWS ECS (Elastic Container Service)
- **ML Services**: AWS SageMaker (planned)
- **Database**: AWS RDS PostgreSQL (production)
- **CI/CD**: GitHub Actions

## 📋 Prerequisites

Before running this application, ensure you have:

- **Node.js** (v16+ recommended)
- **Java JDK** (v17+ required)
- **Maven** (v3.6+ recommended)
- **Spotify Developer Account** with registered application
- **Git** for version control

## 🔧 Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/tbone30/music-recommender.git
cd music-recommender
```

### 2. Spotify API Setup
1. Go to [Spotify Developer Dashboard](https://developer.spotify.com/dashboard)
2. Create a new application
3. Note your `Client ID` and `Client Secret`
4. Add redirect URIs:
   - `http://localhost:8080/api/auth/callback` (backend)
   - `http://localhost:3000/auth/callback` (frontend)

### 3. Backend Setup

#### Configure Environment Variables
```bash
cd backend
cp .env.example .env
```

Edit `.env` file:
```env
# Spotify API Configuration
SPOTIFY_CLIENT_ID=your_spotify_client_id_here
SPOTIFY_CLIENT_SECRET=your_spotify_client_secret_here
SPOTIFY_REDIRECT_URI=http://localhost:8080/api/auth/callback

# Frontend Configuration
FRONTEND_URL=http://localhost:3000

# Database Configuration
DATABASE_URL=jdbc:sqlite:data/spotify_discovery.db
DATABASE_USERNAME=
DATABASE_PASSWORD=

# JWT Configuration
JWT_SECRET=your_jwt_secret_key_here_minimum_32_characters
JWT_EXPIRATION=86400000

# Application Environment
SPRING_PROFILES_ACTIVE=development
```

#### Install Dependencies and Run
```bash
# Install dependencies
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The backend will be available at `http://localhost:8080`

### 4. Frontend Setup

#### Install Dependencies
```bash
cd frontend
npm install
```

#### Configure Environment Variables
```bash
cp .env.example .env
```

Edit `.env` file:
```env
# API Configuration
REACT_APP_API_BASE_URL=http://localhost:8080/api
REACT_APP_API_TIMEOUT=10000

# Spotify Configuration
REACT_APP_SPOTIFY_CLIENT_ID=your_spotify_client_id_here
REACT_APP_SPOTIFY_REDIRECT_URI=http://localhost:3000/auth/callback
REACT_APP_SPOTIFY_SCOPES=user-read-private,user-read-email,playlist-read-private,playlist-modify-public,playlist-modify-private,user-top-read,user-read-recently-played

# Application Configuration
REACT_APP_APP_NAME=Spotify Discovery Platform
REACT_APP_ENVIRONMENT=development
```

#### Install Additional Dependencies
```bash
# Install Tailwind CSS and required packages
npm install -D tailwindcss@latest postcss@latest autoprefixer@latest
npm install -D @tailwindcss/forms @tailwindcss/typography @tailwindcss/aspect-ratio

# Install application dependencies
npm install axios react-router-dom recharts lucide-react
npm install @headlessui/react @heroicons/react react-query react-hot-toast
npm install clsx tailwind-merge
npm install -D @types/react-router-dom
```

#### Run the Application
```bash
npm start
```

The frontend will be available at `http://localhost:3000`

## 📁 Project Structure

```
music-recommender/
├── backend/                          # Spring Boot Backend
│   ├── src/main/java/com/spotifydiscovery/backend/
│   │   ├── config/                   # Configuration classes
│   │   │   ├── SecurityConfig.java   # Security & OAuth2 config
│   │   │   ├── WebClientConfig.java  # HTTP client config
│   │   │   └── SpotifyProperties.java # Spotify API config
│   │   ├── controller/               # REST Controllers
│   │   │   ├── AuthController.java   # Authentication endpoints
│   │   │   ├── UserController.java   # User management
│   │   │   ├── MusicController.java  # Music operations
│   │   │   └── RecommendationController.java # ML endpoints
│   │   ├── entity/                   # JPA Entities
│   │   │   ├── User.java            # User entity
│   │   │   ├── Track.java           # Track entity
│   │   │   ├── UserTrack.java       # User-track interactions
│   │   │   └── Playlist.java        # Playlist entity
│   │   ├── repository/               # Data repositories
│   │   │   ├── UserRepository.java
│   │   │   ├── TrackRepository.java
│   │   │   ├── UserTrackRepository.java
│   │   │   └── PlaylistRepository.java
│   │   ├── service/                  # Business logic
│   │   │   ├── SpotifyService.java   # Spotify API integration
│   │   │   ├── UserService.java      # User operations
│   │   │   ├── RecommendationService.java # ML recommendations
│   │   │   └── PlaylistService.java  # Playlist management
│   │   ├── dto/                      # Data Transfer Objects
│   │   │   ├── request/              # Request DTOs
│   │   │   ├── response/             # Response DTOs
│   │   │   └── spotify/              # Spotify API DTOs
│   │   └── exception/                # Exception handling
│   ├── src/main/resources/
│   │   ├── application.yml           # Application configuration
│   │   └── data/                     # Database files
│   └── pom.xml                       # Maven dependencies
├── frontend/                         # React TypeScript Frontend
│   ├── public/                       # Static assets
│   ├── src/
│   │   ├── components/               # React components
│   │   │   ├── Auth/                 # Authentication components
│   │   │   ├── Layout/               # Layout components
│   │   │   ├── Music/                # Music-related components
│   │   │   ├── Playlist/             # Playlist components
│   │   │   └── UI/                   # Reusable UI components
│   │   ├── contexts/                 # React contexts
│   │   │   ├── AuthContext.tsx       # Authentication state
│   │   │   └── PlayerContext.tsx     # Music player state
│   │   ├── hooks/                    # Custom React hooks
│   │   ├── pages/                    # Page components
│   │   │   ├── Auth/                 # Authentication pages
│   │   │   ├── Dashboard/            # Dashboard page
│   │   │   ├── Discover/             # Music discovery
│   │   │   └── Profile/              # User profile
│   │   ├── services/                 # API services
│   │   │   ├── api.ts                # Main API service
│   │   │   └── spotify.ts            # Spotify-specific calls
│   │   ├── types/                    # TypeScript type definitions
│   │   ├── utils/                    # Utility functions
│   │   └── styles/                   # Global styles
│   ├── tailwind.config.js            # Tailwind CSS configuration
│   ├── postcss.config.js             # PostCSS configuration
│   └── package.json                  # NPM dependencies
├── docs/                             # Documentation
├── scripts/                          # Deployment scripts
├── .env                              # Environment variables
└── README.md                         # This file
```

## 🔑 API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/spotify/callback` - Spotify OAuth callback
- `POST /api/auth/refresh` - Refresh JWT token
- `POST /api/auth/logout` - User logout
- `GET /api/auth/me` - Get current user

### User Management
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `GET /api/users/{id}/preferences` - Get user preferences
- `PUT /api/users/{id}/preferences` - Update preferences
- `GET /api/users/{id}/stats` - Get user statistics

### Music Operations
- `GET /api/music/tracks` - Get tracks (paginated)
- `GET /api/music/tracks/{id}` - Get track by ID
- `GET /api/music/search` - Search tracks
- `POST /api/music/tracks/{id}/rate` - Rate a track
- `POST /api/music/tracks/{id}/favorite` - Add to favorites

### Recommendations
- `POST /api/recommendations` - Get recommendations
- `GET /api/recommendations/users/{id}` - Personalized recommendations
- `GET /api/recommendations/tracks/{id}/similar` - Similar tracks
- `GET /api/recommendations/users/{id}/discovery-playlist` - Discovery playlist

### Playlists
- `GET /api/playlists` - Get playlists (paginated)
- `POST /api/playlists` - Create playlist
- `PUT /api/playlists/{id}` - Update playlist
- `DELETE /api/playlists/{id}` - Delete playlist
- `POST /api/playlists/{id}/tracks` - Add track to playlist

## 🧪 Testing

### Backend Testing
```bash
cd backend
./mvnw test
```

### Frontend Testing
```bash
cd frontend
npm test
```

### Integration Testing
```bash
# Run both backend and frontend
# Then run end-to-end tests
npm run test:e2e
```

## 🚀 Deployment

### Frontend (AWS S3 + CloudFront)
```bash
cd frontend
npm run build
aws s3 sync build/ s3://your-bucket-name/
```

### Backend (AWS ECS)
```bash
cd backend
docker build -t spotify-discovery-backend .
# Push to ECR and deploy to ECS
```

## 🔧 Configuration

### Environment Variables

#### Backend (.env)
- `SPOTIFY_CLIENT_ID` - Spotify application client ID
- `SPOTIFY_CLIENT_SECRET` - Spotify application client secret
- `SPOTIFY_REDIRECT_URI` - OAuth redirect URI
- `DATABASE_URL` - Database connection string
- `JWT_SECRET` - JWT signing secret
- `FRONTEND_URL` - Frontend application URL

#### Frontend (.env)
- `REACT_APP_API_BASE_URL` - Backend API base URL
- `REACT_APP_SPOTIFY_CLIENT_ID` - Spotify client ID
- `REACT_APP_SPOTIFY_REDIRECT_URI` - Frontend OAuth redirect

### Database Configuration

#### Development (SQLite)
- Automatic setup, no configuration needed
- Database file: `backend/data/spotify_discovery.db`

#### Production (PostgreSQL)
Update `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/spotify_discovery
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

## 📊 Machine Learning Features

### Audio Feature Analysis
- **Acousticness**: Confidence measure of whether the track is acoustic
- **Danceability**: How suitable a track is for dancing
- **Energy**: Perceptual measure of intensity and power
- **Valence**: Musical positiveness conveyed by a track

### Recommendation Algorithms
1. **Collaborative Filtering**: User-based and item-based recommendations
2. **Content-Based Filtering**: Audio feature and genre matching
3. **Hybrid Approach**: Combined methods for improved accuracy
4. **Real-time Learning**: Adapts to user behavior and feedback

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Spotify Web API** for providing comprehensive music data
- **Spring Boot** for robust backend framework
- **React** for powerful frontend capabilities
- **Tailwind CSS** for utility-first styling
- **Open Source Community** for amazing libraries and tools

## 📞 Support

For support, questions, or feature requests:
- **GitHub Issues**: [Create an issue](https://github.com/tbone30/music-recommender/issues)
- **Email**: [your-email@example.com]
- **Documentation**: [Project Wiki](https://github.com/tbone30/music-recommender/wiki)

---

**Made with ❤️ by the Spotify Discovery Team**