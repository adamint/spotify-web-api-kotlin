# Testing

We use [Spek](https://github.com/spekframework/spek) to run unit tests. 

To run any test, you must have two environment variables, `SPOTIFY_CLIENT_ID` and `SPOTIFY_CLIENT_SECRET` set to a Spotify application in your current shell.

`export SPOTIFY_CLIENT_ID=your_client_id`
`export SPOTIFY_CLIENT_SECRET=your_client_secret`

To run **only** public endpoint and utility tests, run `gradle check`

To run **all** tests, you need a valid Spotify redirect uri, and token (string). These are `SPOTIFY_REDIRECT_URI` and `SPOTIFY_TOKEN_STRING` respectively.

Then: `gradle check`

Some tests may fail if you do not allow access to all required scopes. To mitigate this, you can individually grant 
each scope or use the following code snippet to print out the Spotify token string (given a generated authorization code)

**How to generate an authorization URL**
```kotlin
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.spotifyClientApi

spotifyClientApi {
    credentials {
        clientId = "YOUR_CLIENT_ID"
        clientSecret = "YOUR_CLIENT_SECRET"
        redirectUri = "YOUR_REDIRECT_URI"
    }
}.getAuthorizationUrl(*SpotifyScope.values())

```

**How to get a Spotify token**
```kotlin
import com.adamratzman.spotify.spotifyClientApi

spotifyClientApi {
    credentials {
        clientId = "YOUR_CLIENT_ID"
        clientSecret = "YOUR_CLIENT_SECRET"
        redirectUri = "YOUR_REDIRECT_URI"
    }
    authorization {
        authorizationCode = "SPOTIFY_AUTHORIZATION_CODE"
    }
}.build().token.accessToken.let { println(it) }
```
