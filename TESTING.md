# Testing

We use [Spek](https://github.com/spekframework/spek) to run unit tests. You must add Maven Central to the gradle repositories 
in order to pull Spek.

You must create a Spotify application [here](https://developer.spotify.com/dashboard/applications) to get credentials.

To run **only** public endpoint tests, run

`gradle check`

Note: You must have `SPOTIFY_CLIENT_ID` and `SPOTIFY_CLIENT_SECRET` as environment variables.

To run **all** tests, you need a valid Spotify application, redirect uri, and token string. use:

`gradle check`

Note: In addition to `SPOTIFY_CLIENT_ID` and `SPOTIFY_CLIENT_SECRET`, you also must have the following environment 
variables set up: `SPOTIFY_REDIRECT_URI` and `SPOTIFY_TOKEN_STRING`

Some tests may fail if you do not allow access to all required scopes. To mitigate this, you can individually grant 
each scope or use the following code snippet to print out the Spotify token string (given a generated authorization code)

**How to generate an authorization URL**
```kotlin
import com.adamratzman.spotify.main.SpotifyScope
val api = spotifyClientApi(
        "SPOTIFY_CLIENT_ID",
        "SPOTIFY_CLIENT_SECRET",
        "SPOTIFY_REDIRECT_URI") {
    authorization {
        tokenString = "SPOTIFY_TOKEN_STRING"
    }
}.getAuthorizationUrl(*SpotifyScope.values())

```

**How to get a Spotify token**
```kotlin
val api = spotifyClientApi(
        "SPOTIFY_CLIENT_ID",
        "SPOTIFY_CLIENT_SECRET",
        "SPOTIFY_REDIRECT_URI") {
    authorization {
        tokenString = "SPOTIFY_TOKEN_STRING"
    }
}.build().token.accessToken.let { println(it) }
```
