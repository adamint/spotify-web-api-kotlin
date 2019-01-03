# Kotlin Spotify Web API
This is the [Kotlin](https://kotlinlang.org/) implementation of the [Spotify Web API](https://developer.spotify.com/web-api/)

## Contents
  1. **[Downloading](#downloading)**
  2. **[Creating a SpotifyAPI or SpotifyClientAPI object](#creating-a-spotifyapi-or-spotifyclientapi-object)**
  3. **[What is the SpotifyRestAction class?](#what-is-the-spotifyrestaction-class?)**
  4. **[Using the Library](#using-the-library)**

## Downloading
This library is available via Maven Central. 

### Maven:
```
<dependency>
    <groupId>com.adamratzman</groupId>
    <artifactId>spotify-api-kotlin</artifactId>
    <version>2.0.1</version>
</dependency>
```

### Gradle
```
compile group: 'com.adamratzman', name: 'spotify-api-kotlin', version: '2.0.1'
```

To use the latest snapshot instead, you must add the Jitpack repository
```
repositories {
	maven { url 'https://jitpack.io' }
}
```
Then, you can use the following:
```
dependencies {
	compile 'com.github.adamint:spotify-web-api-kotlin:dev-SNAPSHOT'
}
```

#### Android
This library should work out of the box on Android.
```

## Creating a SpotifyAPI or SpotifyClientAPI object
In order to use the methods in this library, you must create either a `SpotifyAPI` or `SpotifyClientAPI` object using their respective exposed builders. Client-specific methods are unable to be accessed with the generic SpotifyAPI, rather you must create an instance of the Client API.

### SpotifyAPI
The SpotifyAPI `Token` automatically regenerates when needed.
To build it, you must pass the application id and secret.
```kotlin
import com.adamratzman.spotify.main.SpotifyScope
import com.adamratzman.spotify.main.spotifyApi

spotifyApi {
    credentials {
        clientId = "YOUR_CLIENT_ID"
        clientSecret = "YOUR_CLIENT_SECRET"
    }
}.buildCredentialed()
```
*Note:* You are **unable** to use any client endpoint without authenticating with the methods below. 

### SpotifyClientAPI
All endpoints inside `SpotifyAPI` can be accessed within the `SpotifyClientAPI`.
Its automatic refresh is available *only* when building with
an authorization code or a `Token` object. Otherwise, it will expire `Token#expires_in` seconds after creation.

You have two options when building the Client API.
1. You can use [Implicit Grant access tokens](https://developer.spotify.com/web-api/authorization-guide/#implicit_grant_flow) with 
`Builder.buildToken(token: String)`. However, this is a one-time token that cannot be refreshed.
2. You can use the [Authorization code flow](https://developer.spotify.com/web-api/authorization-guide/#authorization_code_flow). We provide a method
with `Builder.buildAuthCode(code: String, automaticRefresh: Boolean)`to generate the flow url with Builder.getAuthUrl(vararg spotifyScopes: Scope), allowing you to request specific 
spotifyScopes. This library does not provide a method to retrieve the code from your 
callback URL. You must implement that with a web server. This method allows you 
to choose whether to use automatic token refresh.

## What is the SpotifyRestAction class?
I wanted users of this library to have as much flexibility as possible. This 
includes options for asynchronous and blocking execution in all endpoints. However, 
 due to this, you **must** call one of the provided methods in order for the call 
 to execute! The `SpotifyRestAction` provides four methods for use: 1 blocking and 3 async.
- `complete()` blocks the current thread and returns the result
- `queue()` executes and immediately returns
- `queue(consumer: (T) -> Unit)` executes the provided callback as soon as the request 
is asynchronously evaluated
- `queueAfter(quantity: Int, timeUnit: TimeUnit, consumer: (T) -> Unit)` executes the 
provided callback after the provided time. As long as supplier execution is less than the provided 
time, this will likely be accurate within a few milliseconds.
- `asFuture()` transforms the supplier into a `CompletableFuture` 

### SpotifyRestPagingAction
Separate from SpotifyRestAction, this specialized implementation of RestActions is 
just for [PagingObject]. This class gives you the same functionality as SpotifyRestAction, 
but you also have the ability to retrieve *all* of its items or linked PagingObjects with 
a single method call to `getAllItems()` or `getAllPagingObjects()` respectively


## Using the Library
### The benefits of LinkedResults, PagingObjects, and Cursor-based Paging Objects
Spotify provides these three object models in order to simplify our lives as developers. So let's see what we
can do with them!

#### PagingObjects
PagingObjects are a container for the requested objects (`items`), but also include 
important information useful in future calls. It contains the request's `limit` and `offset`, along with 
(sometimes) a link to the next and last page of items and the total number of items returned.

If a link to the next or previous page is provided, we can use the `getNext` and `getPrevious` methods to retrieve 
the respective PagingObjects 

#### Cursor-Based Paging Objects
A cursor-based paging object is a PagingObject with a cursor added on that can be used as a key to find the next 
page of items. The value in the cursor, `after`, describes after what object to begin the query.

Just like with PagingObjects, you can get the next page of items with `getNext`. *However*, there is no 
provided implementation of `after` in this library. You will need to do it yourself, if necessary.

#### LinkedResults
Some endpoints, like `PlaylistsAPI.getPlaylistTracks`, return a LinkedResult, which is a simple wrapper around the 
list of objects. With this, we have access to its Spotify API url (with `href`), and we provide simple methods to parse 
that url.

### Generic Request
For obvious reasons, in most cases, making asynchronous requests via `queue` or `queueAfter` is preferred. However, 
the synchronous format is also shown.

```kotlin
import com.adamratzman.spotify.main.SpotifyScope
import com.adamratzman.spotify.main.spotifyApi

val spotifyApi = spotifyApi {
    credentials {
        clientId = "YOUR_CLIENT_ID"
        clientSecret = "YOUR_CLIENT_SECRET"
    }
}.buildCredentialed()

// block and print out the names of the twenty most similar songs to the search
spotifyApi.search.searchTrack("Début de la Suite").complete().map { it.name }.joinToString().let { println(it) }

// now, let's do it asynchronously
spotifyApi.search.searchTrack("Début de la Suite").queue { it.map { it.name }.joinToString().let { println(it) } }

// simple, right? what about if we want to print ou the featured playlists message from the "Overview" tab?
spotifyApi.browse.getFeaturedPlaylists().complete().message.let { println(it )}

// easy! let's try something a little harder
// let's find out Bénabar's Spotify ID, find his top tracks, and print them out

spotifyApi.search.searchArtist("Bénabar").complete()[0].id.let { id -> 
    spotifyApi.artists.getArtistTopTracks(id).complete().joinToString { it.name }.let { println(it) }
 }
```

### Track Relinking
Spotify keeps many instances of most tracks on their servers, available in different markets. As such, if we use endpoints 
that return tracks, we do not know if these tracks are playable in our market. That's where track relinking comes in.

To relink in a specified market, we must supply a `market` parameter for endpoints where available. 
In both Track and SimpleTrack objects in an endpoint response, there is a nullable field called `linked_from`. 
If the track is unable to be played in the specified market and there is an alternative that *is* playable, this 
will be populated with the href, uri, and, most importantly, the id of the track.

You can then use this track in clientApi actions such as playing or saving the track, knowing that it will be playable 
in your market!

### Contributing
See [CONTRIBUTING.md](CONTRIBUTING.md)

### Endpoint List
#### SpotifyAPI:
   - **[AlbumAPI (SpotifyAPI.albums)](https://developer.spotify.com/web-api/album-endpoints/)**
        1. `getAlbum` returns found Album
        2. `getAlbums` returns found Albums. Use for multiple
        3. `getAlbumTracks` returns a `LinkedResult` (with href to album) of SimpleTracks
   - **[ArtistAPI (SpotifyAPI.artists)](https://developer.spotify.com/web-api/artist-endpoints/)**
        1. `getArtist` returns the found artist
        2. `getArtists` returns a list of Artist objects. Use for multiple.
        3. `getArtistAlbums` returns a `LinkedResult` of SimpleAlbums representing the Artist's albums
        4. `getArtistTopTracks` returns a List of full Track objects
        5. `getRelatedArtists` returns a List of Artist objects relating to the searched artist
   - **[BrowseAPI (SpotifyAPI.browse)](https://developer.spotify.com/web-api/browse-endpoints/)**
        1. `getNewReleases` returns a `PagingObject` of recent Albums
        2. `getFeaturedPlaylists` returns a FeaturedPlaylists object of playlists featured by Spotify
        3. `getCategoryList` returns a `PagingObject` of official Spotify categories
        4. `getCategory` returns a singular SpotifyCategory object by id
        5. `getPlaylistsForCategory` returns a `PagingObject` of top simple playlists for the specified category id
        6. `getRecommendations` returns a RecommendationResponse. Parameters include seed artists, genres, tracks, and 
        tuneable track attributes listed [here](https://developer.spotify.com/web-api/complete-recommendations/)
   - **[PublicFollowingAPI (SpotifyAPI.publicFollowing)](https://developer.spotify.com/web-api/web-api-follow-endpoints/)**
        1. `doUsersFollowPlaylist` returns a List of Booleans corresponding to the order in which ids were specified
   - **[PlaylistsAPI (SpotifyAPI.playlists)](https://developer.spotify.com/web-api/playlist-endpoints/)**
        1. `getPlaylists` returns a `PagingObject` of SimplePlaylists the user has
        2. `getPlaylist` returns a full Playlist object of the specified user and playlist id
        3. `getPlaylistTracks` returns a `LinkedResult` (linked with the playlist url) of **PlaylistTrack**s
        4. `getPlaylistCovers` returns an ordered list of SpotifyImages for the specified playlist.
   - **[SearchAPI (SpotifyAPI.search)](https://developer.spotify.com/web-api/search-item/)**
   It is possible to have 0 results and no exception thrown with these methods. Check the size of items returned.
        1. `searchPlaylist` returns a `PagingObject` of Playlists ordered by likelihood of correct match
        2. `searchTrack` returns a `PagingObject` of **SimpleTrack**s (not tracks!) ordered by likelihood of correct match
        3. `searchArtist` returns a `PagingObject` of Artists ordered by likelihood of correct match.
        4. `searchAlbum` returns a `PagingObject` of **SimpleAlbum**s (not albums!) ordered by likelihood of correct match
   - **[TracksAPI (SpotifyAPI.tracks)](https://developer.spotify.com/web-api/track-endpoints/)**
        1. `getTrack` returns the full Track object of the id searched.
        2. `getTracks` returns an ordered list of Tracks
        3. `getAudioAnalysis` returns the corresponding track analysis. This takes up to a few seconds to process.
        4. `getAudioFeatures` returns the AudioFeatures for the track
        5. `getAudioFeatures(vararg trackIds: String)` returns an ordered list of AudioFeatures. Time is *not* linear by track amount
   - **[PublicUserAPI (SpotifyAPI.users)](https://developer.spotify.com/web-api/user-profile-endpoints/)**
        1. `getProfile` returns the corresponding SpotifyPublicUser object. Pay attention to nullable parameters.
#### SpotifyClientAPI:
Make sure your application has requested the proper [Scopes](https://developer.spotify.com/web-api/using-spotifyScopes/) in order to 
ensure proper function of this library.

Check to see which Scope is necessary with the corresponding endpoint using the 
links provided for each API below
   - **[PersonalizationAPI (SpotifyClientAPI.personalization)](https://developer.spotify.com/web-api/web-api-personalization-endpoints/)**
        1. `getTopArtists` returns an Artist `PagingObject` representing the most played Artists by the user
        2. `getTopTracks` returns a Track `PagingObject` representing the most played Tracks by the user
   - **[ClientUserAPI (SpotifyClientAPI.userProfile)](https://developer.spotify.com/web-api/user-profile-endpoints/)**
        1. `getUserInformation` returns SpotifyUserInformation object, a much more detailed version of the public user object.
   - **[UserLibraryAPI (SpotifyClientAPI.userLibrary)](https://developer.spotify.com/web-api/library-endpoints/)**
        1. `getSavedTracks` returns a `PagingObject` of saved tracks in the user's library
        2. `getSavedAlbums` returns a `PagingObject` of saved albums in the user's library
        3. `savedTracksContains` returns an ordered List of Booleans of whether the track exists in the user's library 
        corresponding to entered ids
        4. `savedAlbumsContains` returns an ordered List of Booleans of whether the album exists in the user's library 
        corresponding to entered ids.
        5. `saveTracks` saves the entered tracks to the user's library. Returns nothing
        6. `saveAlbums` saves the entered albums to the user's library. Returns nothing
        7. `removeSavedTracks` removes the entered tracks from the user's library. Nothing happens if the track is not found. Returns nothing
        8. `removeSavedAlbums` removes the entered albums from the user's library. Nothing happens if the album is not found in the library. Returns nothing
   - **[FollowingAPI (SpotifyClientAPI.userFollowing)](https://developer.spotify.com/web-api/web-api-follow-endpoints/)**
        1. `followingUsers` returns an ordered List of Booleans representing if the user follows the specified users
        2. `followingArtists` returns an ordered List of Booleans representing if the user follows the specified artists
        3. `getFollowedArtists` returns a [Cursor-Based Paging Object](https://developer.spotify.com/web-api/object-model/#cursor-based-paging-object) of followed Artists.
        4. `followUsers` follows the specified users. Cannot be used with artists. Returns nothing
        5. `followArtists` follows the specified artists. Cannot be mixed with users. Returns nothing
        6. `followPlaylist` follows the specified playlist. Returns nothing
        7. `unfollowUsers` unfollows the specified users. Cannot be used with artists. Returns nothing
        8. `unfollowArtists` unfollows the specified artists. Cannot be mixed with users. Returns nothing
        9. `unfollowPlaylist` unfollows the specified playlist. Returns nothing
   - **[PlayerAPI (SpotifyClientAPI.player)](https://developer.spotify.com/web-api/web-api-connect-endpoint-reference/)**
        The methods in this API are in beta and in flux as per Spotify. They will be documented in the near future.
   - **[ClientPlaylistsAPI (SpotifyClientAPI.clientPlaylists)](https://developer.spotify.com/web-api/playlist-endpoints/)**
        1. `createPlaylist` creates the playlist and returns its full Playlist representation
        2. `addTrackToPlaylist` adds the entered tracks to the playlist. Returns nothing
        3. `changePlaylistDescription` changes the description of the playlist. Returns nothing
        4. `getClientPlaylists` returns a `PagingObject` of SimplePlaylists the user has created
        5. `reorderTracks` reorders the tracks in the playlist and returns the current Snapshot
        6. `replaceTracks` replaces tracks in the playlist and returns the current Snapshot
