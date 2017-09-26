# Spotify Kotlin Wrapper
Until we get the JCenter repository running, the repository will be available on Jitpack. Link is below
[![](https://jitpack.io/v/adamint/spotify-web-api-kotlin.svg)](https://jitpack.io/#adamint/spotify-web-api-kotlin)

 
This library represents an updated and more intuitive version of jonasfugedi's Spotify Wrapper, which was created in Java. This is built using Kotlin to take advantage of stlib functionality and Kotlin syntax.

### What this library does
  - Uses the **Client Credentials** authorization type for the Spotify Web API
  - Allows developers to use all non-client endpoints
### What this library does NOT do
  - Handle client OAuth, scopes, or provide wrappers for client endpoints
  - **It currently requires Client ID and Secret to use any method**

# How do I get it?
You **must** have Jitpack in your repositories. An example for gradle is shown below
```
repositories {
			...
			maven { url 'https://jitpack.io' }
		}
```
Then, you can use the following (if you're using gradle - if not, click on the Jitpack link above)
```
dependencies {
	        compile 'com.github.adamint:spotify-web-api-kotlin:-SNAPSHOT'
	}
```

# How do I use this?
You must first create a `SpotifyAPI` object by using the exposed `Builder`, as shown below. Keep in mind, you only need to create one of these!
```kotlin
    val api = SpotifyAPI.Builder("clientId","clientSecret").build()
```
After you've done this, you have access to the following objects:
  - `SpotifyAPI#search` returns a `SearchAPI` object, allowing you to search for tracks, albums, playlists, and artists
  - `SpotifyAPI#albums` returns an `AlbumAPI` object, allowing you to retrieve albums and their tracks
  - `SpotifyAPI#artists` returns an `ArtistsAPI` object, allowing you to retrieve artists by their ids, get their albums and top tracks, and see related artists.
  - `SpotifyAPI#browse` returns a `BrowseAPI` object, allowing you to get new album releases, get featured playlists, get playlists for specific categories, and generate recommendations. The `getRecommendations` method is documented by parameter to avoid confusion.
  - `SpotifyAPI#playlists` returns a `PlaylistsAPI` object,  allowing you to retrieve playlists and their tracks
  - `SpotifyAPI#profiles` returns a `ProfilesAPI` object,  allowing you to retrieve the public user object by a user's id
  - `SpotifyAPI#tracks` returns a `TracksAPI` object,  allowing you to retrieve tracks or get an audio analysis or overview of the track's audio features.
  

### Example using Recommendations

```kotlin
    val api = SpotifyAPI.Builder("yourClientId","yourClientSecret").build()
    val recommendations = api.browse.getRecommendations(seedArtists = listOf("3TVXtAsR1Inumwj472S9r4"), seedGenres = listOf("pop", "country"), targets = hashMapOf(Pair("speechiness", 1.0), Pair("danceability", 1.0))))
```
