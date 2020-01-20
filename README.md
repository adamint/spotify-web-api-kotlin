# Kotlin Spotify Web API 
[![JCenter](https://maven-badges.herokuapp.com/maven-central/com.adamratzman/spotify-api-kotlin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.adamratzman/spotify-api-kotlin)
[![Build Status](http://144.217.240.243:8111/app/rest/builds/aggregated/strob:(buildType:(project:(id:SpotifyWebApiKotlin)))/statusIcon.svg)](http://144.217.240.243:8111/project.html?projectId=SpotifyWebApiKotlin) 
[![](https://img.shields.io/badge/Documentation-latest-orange.svg)](https://adamint.github.io/spotify-web-api-kotlin/docs/spotify-web-api-kotlin/)
![](https://img.shields.io/badge/License-MIT-blue.svg)

This is the [Kotlin](https://kotlinlang.org/) implementation of the [Spotify Web API](https://developer.spotify.com/web-api/)

### Have a question?
If you have a question, you can:

1. Create an [issue](https://github.com/adamint/spotify-web-api-kotlin/issues)
2. Read (but that's hard)
3. Contact me using **Adam#9261** on [Discord](https://discordapp.com) or by sending me an email

## Contents
  1. **[Downloading](#downloading)**
  2. **[Documentation](#documentation)**
  2. **[Creating a SpotifyApi or SpotifyClientApi object](#creating-a-spotifyapi-or-spotifyclientapi-object)**
  3. **[What is the SpotifyRestAction class?](#what-is-the-spotifyrestaction-class?)**
  4. **[Using the Library](#using-the-library)**

## Downloading
This library is available via Maven Central [here](https://search.maven.org/artifact/com.adamratzman/spotify-api-kotlin). 

### Gradle
```
repositories {
    jcenter()
}

compile group: 'com.adamratzman', name: 'spotify-api-kotlin', version: 'SPOTIFY_API_VERSION'
```

To use the latest snapshot instead, you must add the Jitpack repository as well
```
repositories {
    maven { url 'https://jitpack.io' }
    jcenter()
}
```
Then, you can use the following:
```
dependencies {
	compile 'com.github.adamint:spotify-web-api-kotlin:dev-SNAPSHOT'
}
```

### Maven
```
<dependency>
    <groupId>com.adamratzman</groupId>
    <artifactId>spotify-api-kotlin</artifactId>
    <version>SPOTIFY_API_VERSION</version>
</dependency>
```
JCenter Maven Repository:
```
<repository>
    <id>jcenter</id>
    <name>jcenter-bintray</name>
    <url>http://jcenter.bintray.com</url>
</repository>
```

#### Android
This library will work out of the box on Android.

## Documentation
The `spotify-web-api-kotlin` JavaDocs are hosted at https://adamint.github.io/spotify-web-api-kotlin/docs/spotify-web-api-kotlin/

## Samples
Samples for all APIs are located in the `samples` directory

## Creating a SpotifyApi or SpotifyClientApi object
In order to use the methods in this library, you must create either a `SpotifyApi` or `SpotifyClientApi` object using their respective exposed builders. Client-specific methods are unable to be accessed with the generic SpotifyApi, rather you must create an instance of the Client API.

### SpotifyApi
By default, the SpotifyApi `Token` automatically regenerates when needed. This can be changed 
through the `automaticRefresh` parameter in all builders.

To build a new `SpotifyApi`, you must pass the application id and secret.

```kotlin
import com.adamratzman.spotify.SpotifyApi.Companion.spotifyAppApi

val api = spotifyAppApi(
        System.getenv("SPOTIFY_CLIENT_ID"),
        System.getenv("SPOTIFY_CLIENT_SECRET")
).build()
```

*Note:* You are **unable** to use any client endpoint without authenticating with the client methods below. 

#### SpotifyClientApi
The `SpotifyClientApi` is a superset of `SpotifyApi`; thus, you have access to all `SpotifyApi` methods in `SpotifyClientApi`.

Its automatic refresh ability is available *only* when building with
an authorization code or a `Token` object. Otherwise, it will expire `Token.expiresIn` seconds after creation.

You have two options when building the Client API.
1. You can use [Implicit Grant access tokens](https://developer.spotify.com/web-api/authorization-guide/#implicit_grant_flow) by
setting the value of `tokenString` in the builder `authentication` block. However, this is a one-time token that cannot be refreshed.
2. You can use the [Authorization   code flow](https://developer.spotify.com/web-api/authorization-guide/#authorization_code_flow) by 
setting the value of `authorizationCode` in a builder. You may generate an authentication flow url allowing you to request specific 
Spotify scopes using the `getAuthorizationUrl` method in any builder. This library does not provide a method to retrieve the code from your 
callback url; you must implement that with a web server.

## What is the SpotifyRestAction class?
Abstracting requests into a `SpotifyRestAction` class allows for a lot of flexibility in sending and receiving requests. 
This class includes options for asynchronous and blocking execution in all endpoints. However, 
 due to this, you **must** call one of the provided methods in order for the call 
 to execute! The `SpotifyRestAction` provides many methods and fields for use, including blocking and asynchronous ones. For example,
 - `hasRun()` tells you whether the rest action has been *started*
 - `hasCompleted()` tells you whether this rest action has been fully executed and *completed*
- `complete()` blocks the current thread and returns the result
- `suspendComplete(context: CoroutineContext = Dispatchers.Default)` switches to given context, invokes the supplier, and synchronously retrieves the result.
- `suspendQueue()` suspends the coroutine, invokes the supplier asynchronously, and resumes with the result
- `queue()` executes and immediately returns
- `queue(consumer: (T) -> Unit)` executes the provided callback as soon as the request 
is asynchronously evaluated
- `queueAfter(quantity: Int, timeUnit: TimeUnit, consumer: (T) -> Unit)` executes the 
provided callback after the provided time. As long as supplier execution is less than the provided 
time, this will likely be accurate within a few milliseconds.
- `asFuture()` transforms the supplier into a `CompletableFuture` (only JVM)

### SpotifyRestPagingAction
Separate from, but a superset of SpotifyRestAction, this specialized implementation of RestActions is 
just for [AbstractPagingObject] (`PagingObject` and `CursorBasedPagingObject`). This class gives you the same functionality as SpotifyRestAction, 
but you also have the ability to retrieve *all* of its items or linked PagingObjects, or a *subset* of its items or linked PagingObjects with one call, with 
a single method call to `getAllItems()` or `getAllPagingObjects()`, or `getWithNext(total: Int, context: CoroutineContext = Dispatchers.Default)` or `getWithNextItems(total: Int, context: CoroutineContext = Dispatchers.Default)` respectively


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
Some endpoints, like `PlaylistAPI.getPlaylistTracks`, return a LinkedResult, which is a simple wrapper around the 
list of objects. With this, we have access to its Spotify API url (with `href`), and we provide simple methods to parse 
that url.

### Generic Request
For obvious reasons, in most cases, making asynchronous requests via `queue` or `queueAfter` is preferred. However, 
the synchronous format is also shown.

```kotlin
import com.adamratzman.spotify.SpotifyApi.Companion.spotifyAppApi

val api = spotifyAppApi(
        System.getenv("SPOTIFY_CLIENT_ID"),
        System.getenv("SPOTIFY_CLIENT_SECRET")
).build()

// block and print out the names of the twenty most similar songs to the search
println(api.search.searchTrack("Début de la Suite").complete().joinToString { it.name })

// now, let's do it asynchronously
api.search.searchTrack("Début de la Suite").queue { tracks -> println(tracks.joinToString { track -> track.name }) }

// simple, right? what about if we want to print out the featured playlists message from the "Overview" tab?
println(api.browse.getFeaturedPlaylists().complete().message)

// easy! let's try something a little harder
// let's find out Bénabar's Spotify ID, find his top tracks, and print them out

val benabarId = api.search.searchArtist("Bénabar").complete()[0].id
// this works; a redundant way would be: api.artists.getArtist("spotify:artist:6xoAWsIOZxJVPpo7Qvqaqv").complete().id

println(api.artists.getArtistTopTracks(benabarId).complete().joinToString { it.name })
```

### Track Relinking
Spotify keeps many instances of most tracks on their servers, available in different markets. As such, if we use endpoints 
that return tracks, we do not know if these tracks are playable in our market. That's where track relinking comes in.

To relink in a specified market, we must supply a `market` parameter for endpoints where available. 
In both Track and SimpleTrack objects in an endpoint response, there is a nullable field called `linked_from`. 
If the track is unable to be played in the specified market and there is an alternative that *is* playable, this 
will be populated with the href, uri, and, most importantly, the id of the track.

You can then use this track in `SpotifyClientApi` endpoints such as playing or saving the track, knowing that it will be playable 
in your market!

### Contributing
See [CONTRIBUTING.md](CONTRIBUTING.md)

### Endpoint List
#### SpotifyApi:
   - **[AlbumApi (SpotifyApi.albums)](https://developer.spotify.com/web-api/album-endpoints/)**
        1. `getAlbum`
        2. `getAlbums`
        3. `getAlbumTracks`
   - **[ArtistApi (SpotifyApi.artists)](https://developer.spotify.com/web-api/artist-endpoints/)**
        1. `getArtist` 
        2. `getArtists` 
        3. `getArtistAlbums`
        4. `getArtistTopTracks` 
        5. `getRelatedArtists` 
   - **[BrowseApi (SpotifyApi.browse)](https://developer.spotify.com/web-api/browse-endpoints/)**
        1. `getNewReleases`
        2. `getFeaturedPlaylists` 
        3. `getCategoryList` 
        4. `getCategory`
        5. `getPlaylistsForCategory` 
        6. `getTrackRecommendations` 
        7. `getAvailableGenreSeeds`
   - **[FollowingApi (SpotifyApi.following)](https://developer.spotify.com/web-api/web-api-follow-endpoints/)**
        1. `areFollowingPlaylist`
        2. `isFollowingPlaylist`
   - **[PlaylistApi (SpotifyApi.playlist)](https://developer.spotify.com/web-api/playlist-endpoints/)**
        1. `getUserPlaylists` 
        2. `getPlaylist` 
        3. `getPlaylistTracks` 
        4. `getPlaylistCovers` 
   - **[SearchApi (SpotifyApi.search)](https://developer.spotify.com/web-api/search-item/)**
   It is possible to have 0 results and no exception thrown with these methods. Check the size of items returned.
        1. `searchPlaylist` 
        2. `searchTrack` 
        3. `searchArtist`
        4. `searchAlbum` 
        5. `search`
   - **[TrackApi (SpotifyApi.tracks)](https://developer.spotify.com/web-api/track-endpoints/)**
        1. `getTrack` 
        2. `getTracks` 
        3. `getAudioAnalysis` 
        4. `getAudioFeatures` 
   - **[UserApi (SpotifyApi.users)](https://developer.spotify.com/web-api/user-profile-endpoints/)**
        1. `getProfile` 
#### SpotifyClientApi:
Make sure your application has requested the proper [Scopes](https://developer.spotify.com/web-api/using-spotifyScopes/) in order to 
ensure proper function of this library.

Check to see which Scope is necessary with the corresponding endpoint using the 
links provided for each API below
   - **[ClientPersonalizationApi (SpotifyClientApi.personalization)](https://developer.spotify.com/web-api/web-api-personalization-endpoints/)**
        1. `getTopArtists` 
        2. `getTopTracks` 
   - **[ClientProfileApi (SpotifyClientApi.users)](https://developer.spotify.com/web-api/user-profile-endpoints/)**
        1. `getClientProfile` 
   - **[ClientLibraryApi (SpotifyClientApi.library)](https://developer.spotify.com/web-api/library-endpoints/)**
        1. `getSavedTracks`
        2. `getSavedAlbums` 
        3. `contains`
        4. `add`
        5. `remove`
   - **[ClientFollowingApi (SpotifyClientApi.following)](https://developer.spotify.com/web-api/web-api-follow-endpoints/)**
        1. `isFollowingUser` 
        2. `isFollowingPlaylist` 
        3. `isFollowingUsers` 
        4. `isFollowingArtist` 
        5. `isFollowingArtists` 
        6. `getFollowedArtists` 
        7. `followUser` 
        8. `followUsers` 
        9. `followArtist` 
        10. `followArtists`
        11. `followPlaylist`
        12. `unfollowUser`
        13. `unfollowUsers`
        14. `unfollowArtist`
        15. `unfollowArtists`
        16. `unfollowPlaylist`
   - **[ClientPlayerApi (SpotifyClientApi.player)](https://developer.spotify.com/web-api/web-api-connect-endpoint-reference/)**
        1. `getDevices`
        2. `getCurrentContext`
        3. `getRecentlyPlayed`
        4. `getCurrentlyPlaying`
        5. `pause`
        6. `seek`
        7. `setRepeatMode`
        8. `setVolume`
        9. `skipForward`
        10. `skipBehind`
        11. `startPlayback`
        12. `resume`
        13. `toggleShuffle`
        14. `transferPlayback`
   - **[ClientPlaylistApi (SpotifyClientApi.playlists)](https://developer.spotify.com/web-api/playlist-endpoints/)**
        1. `createClientPlaylist`
        2. `addTrackToClientPlaylist` 
        3. `addTracksToClientPlaylist` 
        4. `changeClientPlaylistDetails` 
        5. `getClientPlaylists` 
        6. `getClientPlaylist` 
        7. `deleteClientPlaylist`
        8. `reorderClientPlaylistTracks`
        9. `setClientPlaylistTracks`
        10. `replaceClientPlaylistTracks`
        11. `removeAllClientPlaylistTracks`
        12. `uploadClientPlaylistCover`
        13. `removeTrackFromClientPlaylist`
        14. `removeTracksFromClientPlaylist`