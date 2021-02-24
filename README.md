# Kotlin Spotify Web API 
A [Kotlin](https://kotlinlang.org/) implementation of the [Spotify Web API](https://developer.spotify.com/web-api/),
supporting Kotlin/JS, Kotlin/Android, Kotlin/JVM, and Kotlin/Native
(macOS, Windows, Linux).

[![JCenter](https://maven-badges.herokuapp.com/maven-central/com.adamratzman/spotify-api-kotlin-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.adamratzman/spotify-api-kotlin-core)
[![](https://img.shields.io/badge/Documentation-latest-orange.svg)](https://adamint.github.io/spotify-web-api-kotlin-docs/spotify-web-api-kotlin/)
![](https://img.shields.io/badge/License-MIT-blue.svg)
[![codebeat badge](https://codebeat.co/badges/0ab613b0-31d7-4848-aebc-4ed1e51f069c)](https://codebeat.co/projects/github-com-adamint-spotify-web-api-kotlin-master)


![Android Tests](https://img.shields.io/teamcity/build/s/SpotifyWebApiKotlin_AndroidTests?label=Kotlin%2FAndroid%20Tests&server=https%3A%2F%2Fadam.beta.teamcity.com)
![JS tests Status](https://img.shields.io/teamcity/build/s/SpotifyWebApiKotlin_JsTests?label=Kotlin%2FJS%20Tests&server=https%3A%2F%2Fadam.beta.teamcity.com)
![JVM tests](https://img.shields.io/teamcity/build/s/SpotifyWebApiKotlin_JvmTests?label=Kotlin%2FJVM%20Tests&server=https%3A%2F%2Fadam.beta.teamcity.com)
![Linux Tests](https://img.shields.io/teamcity/build/s/SpotifyWebApiKotlin_LinuxTests?label=Kotlin%2FLinux%20Tests&server=https%3A%2F%2Fadam.beta.teamcity.com)
![macOS Tests](https://img.shields.io/teamcity/build/s/SpotifyWebApiKotlin_RunTestsMac?label=Kotlin%2FmacOS%20Tests&server=https%3A%2F%2Fadam.beta.teamcity.com)

## Table of Contents
* [Library installing](#library-installing)
    + [JVM, Android, JS, Native](#jvm-android-js)
    + [Android information](#android)
* [Documentation](#documentation)
* [Need help, have a question, or want to contribute?](#have-a-question)
* [Creating a new api instance](#creating-a-new-api-instance)
    + [SpotifyAppApi](#spotifyappapi)
    + [SpotifyClientApi](#spotifyclientapi)
        * [PKCE](#pkce)
        * [Non-PKCE](#non-pkce-backend-applications-requires-client-secret)
    + [SpotifyImplicitGrantApi](#spotifyimplicitgrantapi)
    + [SpotifyApiBuilder block & setting API options](#spotifyapibuilder-block--setting-api-options)
        * [API options](#api-options)
    + [Using the API](#using-the-api)
* [Platform-specific wrappers and information](#platform-specific-wrappers-and-information)
    + [JavaScript: Spotify Web Playback SDK wrapper](#js-spotify-web-playback-sdk-wrapper)
* [Tips](#tips)
    + [Building the API](#building-the-api)
* [Notes](#notes)
    + [LinkedResults, PagingObjects, and Cursor-based Paging Objects](#the-benefits-of-linkedresults-pagingobjects-and-cursor-based-paging-objects)
    + [Generic Requests](#generic-request)
    + [Track Relinking](#track-relinking)
* [Contributing](#contributing)

## Library installing
Current version:

[![JCenter](https://maven-badges.herokuapp.com/maven-central/com.adamratzman/spotify-api-kotlin-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.adamratzman/spotify-api-kotlin-core)

### JVM, Android, JS, Native (macOS, Windows, Linux)
```
repositories {
    jcenter()
}

implementation("com.adamratzman:spotify-api-kotlin-core:VERSION")
```

### JS
Please see the [JS Spotify Web Playback SDK wrapper](#js-spotify-web-playback-sdk-wrapper) to learn how to use Spotify's web playback SDK in a browser application.


### Android
**Note**: For information on how to integrate implicit/PKCE authentication, Spotify app remote, and Spotify broadcast notifications into 
your application, please see the [Android README](README_ANDROID.md).


*If you declare any release types not named debug or release, you may see "Could not resolve com.adamratzman:spotify-api-kotlin-android:VERSION". You need to do the following for each release type not named debug or release:*
```
android {
    buildTypes {
        yourReleaseType1 {
            // ...
            matchingFallbacks = ['release', 'debug'] 
        }
        yourReleaseType2 {
            // ...
            matchingFallbacks = ['release', 'debug'] 
        }
	...
    }
}
```


To successfully build, you might need to exclude kotlin_module files from the packaging. To do this, inside the android/buildTypes/release closure, you would put:
```
packagingOptions {
	exclude 'META-INF/*.kotlin_module'
}
```

## Documentation
The `spotify-web-api-kotlin` JavaDocs are hosted [here](https://adamint.github.io/spotify-web-api-kotlin-docs/spotify-web-api-kotlin/).

## Have a question?
If you have a question, you can:

1. Create an [issue](https://github.com/adamint/spotify-web-api-kotlin/issues)
2. Join our [Discord server](https://discord.gg/G6vqP3S)
3. Contact me using **Adam#9261** on [Discord](https://discordapp.com)

## Unsupported features on each platform:
| Feature                     | JVM                | Android            | JS                 | Native (Mac/Windows/Linux) |
|-----------------------------|--------------------|--------------------|--------------------|----------------------------|
| Edit client playlist        | :heavy_check_mark: | :heavy_check_mark: | :heavy_check_mark: | Unsupported                |
| Remove playlist tracks      | :heavy_check_mark: | :heavy_check_mark: | :heavy_check_mark: | Unsupported                |

Please feel free to open an issue/discussion on GitHub or Discord if you need access to one of these features 
or have an interest in implementing one, as direction can be provided.

## Creating a new api instance
To decide which api you need (SpotifyAppApi, SpotifyClientApi, SpotifyImplicitGrantApi), you can refer 
to the sections below or the [Spotify authorization guide](https://developer.spotify.com/documentation/general/guides/authorization-guide/). In general:
- If you don't need client resources, use SpotifyAppApi
- If you're using the api in a backend application, use SpotifyClientApi (with or without PKCE)
- If you're using the api in Kotlin/JS browser, use SpotifyImplicitGrantApi
- If you need access to client resources in an Android or other application, use SpotifyClientApi with PKCE

**Note**: You can use the online [Spotify OAuth Token Generator](https://adamratzman.com/projects/spotify/generate-token) tool to generate a client token for local testing.

### SpotifyAppApi
This provides access only to public Spotify endpoints.
Use this when you have a server-side application. Note that implicit grant authorization 
provides a higher api ratelimit, so consider using implicit grant if your application has 
significant usage.

By default, the SpotifyApi `Token` automatically regenerates when needed. 
This can be changed by overriding the `automaticRefresh` builder setting.

There are four exposed builders, depending on the level of control you need over api creation. 
Please see the [spotifyAppApi builder docs](https://adamint.github.io/spotify-web-api-kotlin-docs/spotify-web-api-kotlin/com.adamratzman.spotify/spotify-app-api.html) for a full list of available builders.

You will need:
- Spotify application client id
- Spotify application client secret

Example creation (default settings)

```kotlin
val api = spotifyAppApi("clientId", "clientSecret").build() // create and build api
println(api.browse.getNewReleases()) // use it
```

Example creation, using an existing Token and setting automatic token refresh to false
```kotlin
val token = spotifyAppApi(spotifyClientId, spotifyClientSecret).build().token
val api = spotifyAppApi(
    "clientId",
    "clientSecret",
    token,
    SpotifyApiOptionsBuilder(
        automaticRefresh = false
    )
)
println(api.browse.getNewReleases()) // use it
```

### SpotifyClientApi
The `SpotifyClientApi` is a superset of `SpotifyApi`; thus, nothing changes if you want to 
access public data.
This library does not provide a method to retrieve the code from your  callback url; instead,
you must implement that with a web server. 
Automatic Token refresh is available *only* when building with an authorization code or a 
`Token` object. Otherwise, it will expire `Token.expiresIn` seconds after creation.

Make sure your application has requested the proper [Scopes](https://developer.spotify.com/web-api/using-spotifyScopes/) in order to 
ensure proper function of this library. The api option `requiredScopes` allows you to verify 
that a client has actually authorized with the scopes you are expecting.

You will need:
- Spotify application client id
- Spotify application client secret (if not using PKCE)
- Spotify application redirect uri
- To choose which client authorization method (PKCE or non-PKCE) to use

#### PKCE
Use the PKCE builders and helper methods if you are using the Spotify client authorization PKCE flow.
Building via PKCE returns a `SpotifyClientApi` which has modified refresh logic.

Use cases:
1. You are using this library in an application (likely Android), or do not want to expose the client secret.

To learn more about the PKCE flow, please read the [Spotify authorization guide](https://developer.spotify.com/documentation/general/guides/authorization-guide/#implicit-grant-flow).
Some highlights about the flow are:
- It is refreshable, but each refresh token can only be used once. This library handles token refresh automatically by default
- It does not require a client secret; instead, a set redirect uri and a random code verifier 
are used to verify the authenticity of the authorization.
- A code verifier is required. The code verifier is "*a cryptographically random string between 43 and 128 characters in length. 
It can contain letters, digits, underscores, periods, hyphens, or tildes.*"
- A code challenge is required. "*In order to generate the code challenge, your app should 
hash the code verifier using the SHA256 algorithm. Then, base64url encode the hash that you generated.*"
- When creating a pkce api instance, the code verifier is passed in by you and compared to 
the code challenge used to authorize the user.

This library contains helpful methods that can be used to simplify the PKCE authorization process.
This includes `getSpotifyPkceCodeChallenge`, which SHA256 hashes and base64url encodes the code 
challenge, and `getPkceAuthorizationUrl`, which allows you to generate an easy authorization url for PKCE flow.

Please see the [spotifyClientPkceApi builder docs](https://adamint.github.io/spotify-web-api-kotlin-docs/spotify-web-api-kotlin/com.adamratzman.spotify/spotify-client-pkce-api.html) for a full list of available builders.
 
**Takeaway**: Use PKCE authorization flow in applications where you cannot secure the client secret.

To get a PKCE authorization url, to which you can redirect a user, you can use the `getPkceAuthorizationUrl`
top-level method. An example is shown below, requesting 4 different scopes.
```kotlin
val codeVerifier = "thisisaveryrandomalphanumericcodeverifierandisgreaterthan43characters"
val codeChallenge = getSpotifyPkceCodeChallenge(codeVerifier) // helper method
val url: String = getPkceAuthorizationUrl(
    SpotifyScope.PLAYLIST_READ_PRIVATE,
    SpotifyScope.PLAYLIST_MODIFY_PRIVATE,
    SpotifyScope.USER_FOLLOW_READ,
    SpotifyScope.USER_LIBRARY_MODIFY,
    clientId = "clientId",
    redirectUri = "your-redirect-uri",
    codeChallenge = codeChallenge
)
```

There is also an optional parameter `state`, which helps you verify the authorization.

**Note**: If you want automatic token refresh, you need to pass in your application client id and redirect uri 
when using the `spotifyClientPkceApi`.

##### Example: A user has authorized your application. You now have the authorization code obtained after the user was redirected back to your application. You want to create a new `SpotifyClientApi`.
```kotlin
val codeVerifier = "thisisaveryrandomalphanumericcodeverifierandisgreaterthan43characters"
val code: String = ...
val api = spotifyClientPkceApi(
    "clientId", // optional. include for token refresh
    "your-redirect-uri", // optional. include for token refresh
    code,
    codeVerifier, // the same code verifier you used to generate the code challenge
    SpotifyApiOptionsBuilder(
        retryWhenRateLimited = false
    )
).build()
println(api.library.getSavedTracks().take(10).filterNotNull().map { it.track.name })
```

#### Non-PKCE (backend applications, requires client secret)
To get a non-PKCE authorization url, to which you can redirect a user, you can use the `getSpotifyAuthorizationUrl`
top-level method. An example is shown below, requesting 4 different scopes.
```kotlin
val url: String = getSpotifyAuthorizationUrl(
    SpotifyScope.PLAYLIST_READ_PRIVATE,
    SpotifyScope.PLAYLIST_MODIFY_PRIVATE,
    SpotifyScope.USER_FOLLOW_READ,
    SpotifyScope.USER_LIBRARY_MODIFY,
    clientId = "clientId",
    redirectUri = "your-redirect-uri",
    state = "your-special-state" // optional
)
```
There are also several optional parameters, allowing you to set whether the authorization url is meant 
for implicit grant flow, the state, and whether a re-authorization dialog should be shown to users.

There are several exposed builders, depending on the level of control you need over api creation. 
Please see the [spotifyClientApi builder docs](https://adamint.github.io/spotify-web-api-kotlin-docs/spotify-web-api-kotlin/com.adamratzman.spotify/spotify-client-api.html) for a full list of available builders.

##### Example: You've redirected the user back to your web server and have an authorization code (code).
In this example, automatic token refresh is turned on by default.
```kotlin
val authCode = ""
val api = spotifyClientApi(
    "clientId",
    "clientSecret",
    "your-redirect-uri",
    authCode
).build() // create and build api
println(api.personalization.getTopTracks(limit = 5).items.map { it.name }) // print user top tracks
```

##### Example: You've saved a user's token from previous authorization and need to create an api instance.
In this case, if you provide a client id to the builder, automatic token refresh will also be turned on.
```kotlin
val token: Token = ... // your existing token
val api = spotifyClientApi(
    "clientId",
    "clientSecret",
    "your-redirect-uri",
    token,
    SpotifyApiOptionsBuilder(
        onTokenRefresh = { 
            println("Token refreshed at ${System.currentTimeMillis()}")
        }
    )
).build()
println(api.personalization.getTopTracks(limit = 5).items.map { it.name })
```


### SpotifyImplicitGrantApi
Use the `SpotifyImplicitGrantApi` if you are using the Spotify implicit grant flow.
`SpotifyImplicitGrantApi` is a superset of `SpotifyClientApi`.
Unlike the other builders, the `spotifyImplicitGrantApi` builder method directly returns 
a `SpotifyImplicitGrantApi` instead of an api builder.

Use cases:
1. You are using the **Kotlin/JS** target for this library.
2. Your frontend Javascript passes the token received through the implicit grant flow to your 
backend, where it is then used to create an api instance.

To learn more about the implicit grant flow, please read the [Spotify authorization guide](https://developer.spotify.com/documentation/general/guides/authorization-guide/#implicit-grant-flow).
Some highlights about the flow are:
- It is non-refreshable
- It is client-side
- It does not require a client secret

Please see the [spotifyImplicitGrantApi builder docs](https://adamint.github.io/spotify-web-api-kotlin-docs/spotify-web-api-kotlin/com.adamratzman.spotify/spotify-implicit-grant-api.html) for a full list of available builders.
 
The Kotlin/JS target contains the `parseSpotifyCallbackHashToToken` method, which will parse the hash 
for the current url into a Token object, with which you can then instantiate the api.

**Takeaway**: There are two ways to use implicit grant flow, browser-side only and browser and 
server. This library provides easy access for both.

##### Example
```kotlin
val token: Token = ...
val api = spotifyImplicitGrantApi(
    null,
    null,
    token
) // create api. there is no need to build it 
println(api.personalization.getTopArtists(limit = 1)[0].name) // use it
```

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
- `requiredScopes`: Scopes that your application requires to function (only applicable to `SpotifyClientApi` and `SpotifyImplicitGrantApi`).
This verifies that the token your user authorized with actually contains the scopes your 
application needs to function.

Notes:
- Unless you have a good reason otherwise, `useCache` should be true
- `cacheLimit` is per Endpoint, not per API. Don't be surprised if you end up with over 200 items in your cache with the default settings.
- `automaticRefresh` is disabled when client secret is not provided, or if tokenString is provided in SpotifyClientApi
- `allowBulkRequests` for example, lets you query 80 artists in one wrapper call by splitting it into 50 artists + 30 artists
- `refreshTokenProducer` is useful when you want to re-authorize with the Spotify Auth SDK or elsewhere

### Using the API
APIs available in all `SpotifyApi` instances, including `SpotifyClientApi` and `SpotifyImplicitGrantApi`:
- `SearchApi` (searching items)
- `AlbumApi` (get information about albums)
- `BrowseApi` (browse new releases, featured playlists, categories, and recommendations)
- `ArtistApi` (get information about artists)
- `PlaylistApi` (get information about playlists)
- `UserApi` (get public information about users on Spotify)
- `TrackApi` (get information about tracks)
- `FollowingApi` (check whether users follow playlists)

APIs available only in `SpotifyClientApi` and `SpotifyImplicitGrantApi` instances:
- `ClientSearchApi` (all the methods in `SearchApi`, and searching shows and episodes)
- `EpisodeApi` (get information about episodes)
- `ShowApi` (get information about shows)
- `ClientPlaylistApi` (all the methods in `PlaylistApi`, and get and manage user playlists)
- `ClientProfileApi` (all the methods in `UserApi`, and get the user profile, depending on scopes)
- `ClientFollowingApi` (all the methods in `FollowingApi`, and get and manage following of playlists, artists, and users)
- `ClientPersonalizationApi` (get user top tracks and artists)
- `ClientLibraryApi` (get and manage saved tracks and albums)
- `ClientPlayerApi` (view and control Spotify playback)

## Platform-specific wrappers and information

### Android authentication
For information on how to integrate implicit/PKCE authentication, Spotify app remote, and Spotify broadcast notifications into 
your application, please see the [Android README](README_ANDROID.md).

### JS Spotify Web Playback SDK wrapper
`spotify-web-api-kotlin` provides a wrapper around Spotify's [Web Playback SDK](https://developer.spotify.com/documentation/web-playback-sdk/reference/) 
for playing music via Spotify in the browser on your own site.

To do this, you need to create a `Player` instance and then use the associated methods to register listeners, play, 
and get current context.

Please see an example of how to do this [here](https://github.com/adamint/spotify-web-api-browser-example/blob/95df60810611ddb961a7a2cb0c874a76d4471aa7/src/main/kotlin/com/adamratzman/layouts/HomePageComponent.kt#L38). 
An example project, [spotify-web-api-browser-example](https://github.com/adamint/spotify-web-api-browser-example), 
demonstrates how to create a frontend JS Kotlin application with Spotify integration and 
that will play music in the browser.

**Notes**:
1. You must include the Spotify player JS script by including `<script src="https://sdk.scdn.co/spotify-player.js"></script>`
2. You must define a `window.onSpotifyWebPlaybackSDKReady` function immediately afterwards - this should load your main application bundle.
    Otherwise, you will get errors. An example is below:
   
```html
<html>
<head>
    ...
    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>

    <script>
        jQuery.loadScript = function (url, callback) {
            jQuery.ajax({
                url: url,
                dataType: 'script',
                success: callback,
                async: true
            });
        }
    </script>

    <script src="https://sdk.scdn.co/spotify-player.js"></script>
    <script>
        window.onSpotifyWebPlaybackSDKReady = () => {
            $.loadScript("main.bundle.js")
        }
    </script>
</head>
<body>
....
</body>
</html>
```

## Tips

### Building the API
The easiest way to build the API is using .build() after a builder
```kotlin
runBlocking {
    val api = spotifyAppApi(clientId, clientSecret).build()
}
```

## Notes
### Re-authentication
If you are using an authorization flow or token that does not support automatic token refresh, `SpotifyException.ReAuthenticationNeededException` 
will be thrown. You should put your requests, if creating an application, behind a try/catch block to re-authenticate users if this 
exception is thrown.

### LinkedResults, PagingObjects, and Cursor-based Paging Objects
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

// print out the names of the twenty most similar songs to the search
println(api.search.searchTrack("Début de la Suite").joinToString { it.name })

// simple, right? what about if we want to print out the featured playlists message from the "Overview" tab?
println(api.browse.getFeaturedPlaylists().message)

// easy! let's try something a little harder
// let's find out Bénabar's Spotify ID, find his top tracks, and print them out
val benabarId = api.search.searchArtist("Bénabar")[0].id
// this works; a redundant way would be: api.artists.getArtist("spotify:artist:6xoAWsIOZxJVPpo7Qvqaqv").id
println(api.artists.getArtistTopTracks(benabarId).joinToString { it.name })
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

## Contributing
See [CONTRIBUTING.md](CONTRIBUTING.md)
