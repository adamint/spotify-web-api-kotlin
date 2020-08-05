# Kotlin Spotify Web API 
[![JCenter](https://maven-badges.herokuapp.com/maven-central/com.adamratzman/spotify-api-kotlin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.adamratzman/spotify-api-kotlin)
[![Build Status](http://144.217.240.243:8111/app/rest/builds/aggregated/strob:(buildType:(project:(id:SpotifyWebApiKotlin)))/statusIcon.svg)](http://144.217.240.243:8111/project.html?projectId=SpotifyWebApiKotlin) 
[![](https://img.shields.io/badge/Documentation-latest-orange.svg)](https://adamint.github.io/spotify-web-api-kotlin/docs/spotify-web-api-kotlin/)
![](https://img.shields.io/badge/License-MIT-blue.svg)

This is the [Kotlin](https://kotlinlang.org/) implementation of the [Spotify Web API](https://developer.spotify.com/web-api/)

## Install it

### JVM
This library is available via JCenter [here](https://search.maven.org/artifact/com.adamratzman/spotify-api-kotlin). 

#### Gradle
```
repositories {
    jcenter()
}

compile group: 'com.adamratzman', name: 'spotify-api-kotlin', version: '3.2.02'
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

### Android
```
repositories {
    jcenter()
}

compile group: 'com.adamratzman', name: 'spotify-api-kotlin-android', version: '3.2.02'
```

To successfully build, you might need to exclude kotlin_modules from the packaging. To do this, inside the android/buildTypes/release closure, you would put:
```
packagingOptions {
	exclude 'META-INF/*.kotlin_module'
}
```

### Kotlin/JS
```
repositories {
    jcenter()
}

compile group: 'com.adamratzman', name: 'spotify-api-kotlin-js', version: '3.2.02'
```

## Documentation
The `spotify-web-api-kotlin` JavaDocs are hosted at https://adamint.github.io/spotify-web-api-kotlin/com.adamratzman.spotify-web-api-kotlin/

## Samples
Samples for all APIs are located in the `samples` directory

## Have a question?
If you have a question, you can:

1. Create an [issue](https://github.com/adamint/spotify-web-api-kotlin/issues)
2. Join our [Discord server](https://discord.gg/G6vqP3S)
3. Contact me using **Adam#9261** on [Discord](https://discordapp.com)

## Creating a new api instance
To decide which api you need (SpotifyAppApi, SpotifyClientApi, SpotifyImplicitGrantApi), please refer to 
https://developer.spotify.com/documentation/general/guides/authorization-guide/. In general:
- If you don't need client resources, use SpotifyAppApi
- If you're using the api in a backend application, use SpotifyClientApi
- If you're using the api in a frontend application, use SpotifyImplicitGrantApi

### SpotifyAppApi
This provides access only to public Spotify endpoints. By default, the SpotifyApi `Token` automatically regenerates when needed. This can be changed 
through the `automaticRefresh` parameter in all builders.

There are four exposed builders, depending on the level of control you need over api creation. 
Please see the [spotifyAppApi builder docs](https://adamint.github.io/spotify-web-api-kotlin/docs/spotify-web-api-kotlin/com.adamratzman.spotify/spotify-app-api.html) for a full list of available builders, or the app api [samples](https://github.com/adamint/spotify-web-api-kotlin/tree/master/samples/jvm/src/main/kotlin/appApi). 
 

### SpotifyClientApi
The `SpotifyClientApi` is a superset of `SpotifyApi`; thus, you have access to all `SpotifyApi` methods in `SpotifyClientApi`. 
This library does not provide a method to retrieve the code from your  callback url; you must implement that with a web server.

Make sure your application has requested the proper [Scopes](https://developer.spotify.com/web-api/using-spotifyScopes/) in order to 
ensure proper function of this library.

Its automatic refresh ability is available *only* when building with
an authorization code or a `Token` object. Otherwise, it will expire `Token.expiresIn` seconds after creation.

Please see the [spotifyClientApi builder docs](https://adamint.github.io/spotify-web-api-kotlin/docs/spotify-web-api-kotlin/com.adamratzman.spotify/spotify-client-api.html) for a full list of available builders, or the client [samples](https://github.com/adamint/spotify-web-api-kotlin/tree/master/samples/jvm/src/main/kotlin/clientApi). 

### SpotifyImplicitGrantApi
Instantiate this api only if you are using the Spotify implicit grant flow. It is a superset of `SpotifyClientApi`.
Please see the [spotifyImplicitGrantApi builder docs](https://adamint.github.io/spotify-web-api-kotlin/docs/spotify-web-api-kotlin/com.adamratzman.spotify/spotify-implicit-grant-api.html) for a full list of available builders, or the implicit grant [samples (tbd)](). 

### SpotifyApiBuilder Block & setting API options 
There are three pluggable blocks in each api's corresponding builder

1. `credentials` lets you set the client id, client secret, and redirect uri
2. `authorization` lets you set the type of api authorization you are using. 
Acceptable types include: an authorization code, a `Token` object, a Token's access code string, and an optional refresh token string
3. `options` lets you configure API options to your own specific needs

#### API options
This library does not attempt to be prescriptivist. 
All API options are located in `SpotifyApiOptions` and their default values can be overridden; however, use caution in doing so, as 
most of the default values either allow for significant performance or feature enhancements to the API instance.

- `useCache`: Set whether to cache requests. Default: true
- `cacheLimit`: The maximum amount of cached requests allowed at one time. Null means no limit. Default: 200
- `automaticRefresh`: Enable or disable automatic refresh of the Spotify access token when it expires. Default: true
- `retryWhenRateLimited`: Set whether to block the current thread and wait until the API can retry the request. Default: true
- `enableLogger`: Set whether to enable to the exception logger. Default: true
- `testTokenValidity`: After API creation, test whether the token is valid by performing a lightweight request. Default: false
- `defaultLimit`: The default amount of objects to retrieve in one request. Default: 50
- `json`: The Json serializer/deserializer instance.
- `allowBulkRequests`: Allow splitting too-large requests into smaller, allowable api requests. Default: true 
- `requestTimeoutMillis`: The maximum time, in milliseconds, before terminating an http request. Default: 100000ms
- `refreshTokenProducer`: Provide if you want to use your own logic when refreshing a Spotify token.

Notes:
- Unless you have a good reason otherwise, `useCache` should be true
- `cacheLimit` is per Endpoint, not per API. Don't be surprised if you end up with over 200 items in your cache with the default settings.
- `automaticRefresh` is disabled when client secret is not provided, or if tokenString is provided in SpotifyClientApi
- `allowBulkRequests` for example, lets you query 80 artists in one wrapper call by splitting it into 50 artists + 30 artists
- `refreshTokenProducer` is useful when you want to re-authorize with the Spotify Auth SDK or elsewhere

### Building the API
The easiest way to build the API is synchronously using .build() after a builder

```kotlin
spotifyAppApi(clientId, clientSecret).build()
```

You can also build the API asynchronously using kotlin coroutines!
```kotlin
runBlocking {
    spotifyAppApi(clientId, clientSecret).buildAsyncAt(this) { api ->
        // do things
    }
}
```

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
// this works; a redundant way would be: api.artists.getArtist("com.adamratzman.spotify:artist:6xoAWsIOZxJVPpo7Qvqaqv").complete().id

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
