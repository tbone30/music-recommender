# Music Recommender Backend - Development Roadmap

## 🎯 Current Status
✅ **COMPLETED:**
- ✅ Spring Boot project setup with Maven
- ✅ Spotify API integration and test connection
- ✅ OAuth2 configuration for Spotify
- ✅ SQLite database configuration
- ✅ Basic project structure
- ✅ DevTools added for hot reload

## 🚀 Next Development Steps

### Phase 1: Core Data Models & Authentication (Week 1-2)

#### 1.1 Create Entity Classes
**Priority: HIGH**

Create the following JPA entities in `src/main/java/com/musicrecommender/backend/entity/`:

- [ ] **User.java** - Store user profile and preferences
  ```java
  // Fields: id, spotifyId, displayName, email, country, profileImageUrl, createdAt, lastLoginAt
  ```

- [ ] **Track.java** - Spotify track information
  ```java
  // Fields: id, spotifyId, name, artists, album, durationMs, popularity, previewUrl, externalUrls
  ```

- [ ] **Artist.java** - Artist information
  ```java
  // Fields: id, spotifyId, name, genres, popularity, followers, imageUrl
  ```

- [ ] **Album.java** - Album information
  ```java
  // Fields: id, spotifyId, name, artists, releaseDate, totalTracks, imageUrl
  ```

- [ ] **Playlist.java** - User playlists
  ```java
  // Fields: id, spotifyId, name, description, userId, isPublic, trackCount, createdAt, updatedAt
  ```

- [ ] **UserListeningHistory.java** - Track user listening patterns
  ```java
  // Fields: id, userId, trackId, playedAt, playDuration, skipReason
  ```

- [ ] **Recommendation.java** - Store generated recommendations
  ```java
  // Fields: id, userId, trackId, score, reasonType, generatedAt, wasAccepted
  ```

#### 1.2 Create Repository Interfaces
**Priority: HIGH**

Create repositories in `src/main/java/com/musicrecommender/backend/repository/`:

- [ ] **UserRepository.java**
- [ ] **TrackRepository.java** 
- [ ] **ArtistRepository.java**
- [ ] **AlbumRepository.java**
- [ ] **PlaylistRepository.java**
- [ ] **UserListeningHistoryRepository.java**
- [ ] **RecommendationRepository.java**

#### 1.3 Implement Authentication Flow
**Priority: HIGH**

- [ ] Create **AuthController.java** with endpoints:
  - `GET /auth/login` - Initiate Spotify OAuth
  - `GET /auth/callback` - Handle OAuth callback
  - `POST /auth/logout` - User logout
  - `GET /auth/me` - Get current user info

- [ ] Create **AuthService.java** for:
  - OAuth token management
  - User session handling
  - JWT token generation (optional)

- [ ] Update **SpotifyIntegrationService.java** to handle user tokens

### Phase 2: Spotify Data Integration (Week 2-3)

#### 2.1 User Profile & Preferences
**Priority: HIGH**

- [ ] Create **UserService.java** with methods:
  - `getUserProfile(String accessToken)`
  - `saveUserToDatabase(SpotifyUser spotifyUser)`
  - `getUserTopTracks(String accessToken, String timeRange)`
  - `getUserTopArtists(String accessToken, String timeRange)`

- [ ] Create **UserController.java** with endpoints:
  - `GET /users/profile` - Get user profile
  - `GET /users/top-tracks` - Get user's top tracks
  - `GET /users/top-artists` - Get user's top artists
  - `PUT /users/preferences` - Update user preferences

#### 2.2 Music Data Services
**Priority: HIGH**

- [ ] Enhance **SpotifyIntegrationService.java** with:
  - `getTrackDetails(String trackId)`
  - `getArtistDetails(String artistId)`
  - `getAlbumDetails(String albumId)`
  - `searchTracks(String query, int limit)`
  - `getTrackAudioFeatures(String trackId)`
  - `getRecommendations(List<String> seedTracks, Map<String, Object> parameters)`

- [ ] Create **MusicDataService.java** for:
  - Caching Spotify data in local database
  - Batch processing of track/artist data
  - Data synchronization

#### 2.3 Playlist Management
**Priority: MEDIUM**

- [ ] Create **PlaylistService.java** with:
  - `getUserPlaylists(String accessToken)`
  - `getPlaylistTracks(String playlistId)`
  - `createPlaylist(String userId, PlaylistRequest request)`
  - `addTracksToPlaylist(String playlistId, List<String> trackIds)`

- [ ] Create **PlaylistController.java** with endpoints:
  - `GET /playlists` - Get user playlists
  - `GET /playlists/{id}` - Get playlist details
  - `POST /playlists` - Create new playlist
  - `PUT /playlists/{id}/tracks` - Add tracks to playlist

### Phase 3: Recommendation Engine (Week 3-4)

#### 3.1 Basic Recommendation Logic
**Priority: HIGH**

- [ ] Create **RecommendationService.java** with:
  - `generateRecommendations(String userId)`
  - `getRecommendationsByGenre(String userId, String genre)`
  - `getRecommendationsByMood(String userId, String mood)`
  - `getSimilarTracks(String trackId)`

- [ ] Implement recommendation algorithms:
  - **Collaborative Filtering** - Based on similar users
  - **Content-Based** - Based on audio features
  - **Hybrid Approach** - Combine multiple methods

- [ ] Create **RecommendationController.java** with endpoints:
  - `GET /recommendations` - Get personalized recommendations
  - `GET /recommendations/genres/{genre}` - Genre-based recommendations
  - `GET /recommendations/similar/{trackId}` - Similar tracks
  - `POST /recommendations/{id}/feedback` - User feedback on recommendations

#### 3.2 Audio Features Analysis
**Priority: MEDIUM**

- [ ] Create **AudioFeaturesService.java** for:
  - Analyzing track characteristics (energy, valence, danceability, etc.)
  - Mood detection based on audio features
  - Genre classification

- [ ] Implement mood-based recommendations:
  - Happy/Upbeat, Sad/Melancholic, Energetic, Chill, etc.

### Phase 4: Advanced Features (Week 4-5)

#### 4.1 User Behavior Tracking
**Priority: MEDIUM**

- [ ] Create **ListeningHistoryService.java** for:
  - Tracking play counts and skip rates
  - Analyzing listening patterns
  - Time-based preference analysis

- [ ] Create **AnalyticsController.java** with endpoints:
  - `GET /analytics/listening-stats` - User listening statistics
  - `GET /analytics/mood-trends` - Mood analysis over time
  - `GET /analytics/discovery-stats` - Music discovery metrics

#### 4.2 Social Features (Optional)
**Priority: LOW**

- [ ] Create **SocialService.java** for:
  - Following other users
  - Sharing playlists
  - Social recommendations

#### 4.3 Advanced Search & Discovery
**Priority: MEDIUM**

- [ ] Create **SearchService.java** with:
  - Multi-faceted search (artist, genre, mood, year, etc.)
  - Search result ranking
  - Search history and suggestions

- [ ] Create **SearchController.java** with endpoints:
  - `GET /search/tracks` - Advanced track search
  - `GET /search/artists` - Artist search
  - `GET /search/suggestions` - Search suggestions

### Phase 5: Performance & Optimization (Week 5-6)

#### 5.1 Caching Strategy
**Priority: HIGH**

- [ ] Implement Redis caching for:
  - Spotify API responses
  - User recommendations
  - Frequently accessed data

- [ ] Add caching annotations and configuration

#### 5.2 Background Jobs
**Priority: MEDIUM**

- [ ] Create scheduled tasks for:
  - Updating user listening data
  - Refreshing recommendations
  - Cleaning up old data

- [ ] Implement async processing for heavy operations

#### 5.3 API Rate Limiting & Error Handling
**Priority: HIGH**

- [ ] Implement proper error handling:
  - Spotify API rate limiting
  - Network timeout handling
  - Database connection issues

- [ ] Add comprehensive logging and monitoring

### Phase 6: Testing & Documentation (Ongoing)

#### 6.1 Unit Testing
**Priority: HIGH**

- [ ] Write unit tests for all services
- [ ] Mock Spotify API calls in tests
- [ ] Achieve >80% code coverage

#### 6.2 Integration Testing
**Priority: MEDIUM**

- [ ] Test full authentication flow
- [ ] Test recommendation generation
- [ ] Test database operations

#### 6.3 API Documentation
**Priority: MEDIUM**

- [ ] Add Swagger/OpenAPI documentation
- [ ] Create API usage examples
- [ ] Document authentication flow

## 🛠 Technical Considerations

### Database Schema Design
```sql
-- Key tables to implement:
-- users, tracks, artists, albums, playlists
-- user_listening_history, recommendations
-- user_preferences, playlist_tracks
```

### Environment Variables to Add
```properties
# Add to .env file:
SPOTIFY_CLIENT_ID=your_client_id
SPOTIFY_CLIENT_SECRET=your_client_secret
SPOTIFY_REDIRECT_URI=http://localhost:8080/api/auth/callback
FRONTEND_URL=http://localhost:3000
JWT_SECRET=your_jwt_secret
REDIS_URL=redis://localhost:6379
```

### Recommended Dependencies to Add
- **Redis** for caching: `spring-boot-starter-data-redis`
- **JWT** for tokens: `java-jwt`
- **Swagger** for docs: `springdoc-openapi-starter-webmvc-ui`
- **Testing**: `testcontainers` for integration tests

## 🎯 Success Metrics

### Phase 1 Success Criteria:
- [ ] User can authenticate with Spotify
- [ ] User profile data is stored in database
- [ ] Basic CRUD operations work

### Phase 2 Success Criteria:
- [ ] User's Spotify data is imported
- [ ] Playlists and tracks are accessible
- [ ] API responses are properly structured

### Phase 3 Success Criteria:
- [ ] Basic recommendations are generated
- [ ] User can provide feedback
- [ ] Recommendations improve over time

## 🚨 Important Notes

1. **API Rate Limits**: Spotify has strict rate limits. Implement proper caching and request throttling.

2. **User Privacy**: Store only necessary user data and respect Spotify's terms of service.

3. **Token Management**: Implement proper token refresh mechanisms for long-lived sessions.

4. **Data Sync**: Consider when and how often to sync with Spotify API vs. using cached data.

5. **Scalability**: Design with future scaling in mind (consider microservices architecture later).

## 📁 Next Immediate Steps

1. **Start with Phase 1.1** - Create the User entity and UserRepository
2. **Set up basic authentication flow** - This unblocks frontend development
3. **Create a simple recommendation endpoint** - Even with mock data initially
4. **Add proper error handling and logging** - Essential for debugging

---

**Estimated Timeline**: 5-6 weeks for full implementation
**Recommended Team Size**: 1-2 backend developers
**Priority Focus**: Authentication → Data Models → Basic Recommendations → Advanced Features
