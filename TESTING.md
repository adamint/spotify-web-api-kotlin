# Testing

We use [Spek](https://github.com/spekframework/spek) to run unit tests.

To run **only** public endpoint tests, run

`gradle test -PclientId=YOUR_CLIENT_ID -PclientSecret=YOUR_CLIENT_SECRET`

To run **all** tests, you need a valid Spotify application, redirect uri, and token string. use:

`gradle test -PclientId=YOUR_CLIENT_ID -PclientSecret=YOUR_CLIENT_SECRET -PspotifyRedirectUri=SPOTIFY_REDIRECT_URI -PspotifyTokenString=SPOTIFY_TOKEN`

Some tests may fail if you do not allow access to all required scopes. To mitigate this, you can individually grant 
each scope or use the following code snippet to print out the Spotify token string (given a generated authorization code)

**How to generate an authorization URL**
```kotlin
import com.adamratzman.spotify.main.SpotifyScope
import com.adamratzman.spotify.main.spotifyApi

spotifyApi {
    credentials {
        clientId = "YOUR_CLIENT_ID"
        clientSecret = "YOUR_CLIENT_SECRET"
        redirectUri = "YOUR_REDIRECT_URI"
    }
}.getAuthorizationUrl(*SpotifyScope.values())

```

**How to get a Spotify token**
```kotlin
import com.adamratzman.spotify.main.spotifyApi

spotifyApi { 
    credentials { 
        clientId = "YOUR_CLIENT_ID"
        clientSecret = "YOUR_CLIENT_SECRET"
        redirectUri = "YOUR_REDIRECT_URI"
    }
    clientAuthentication { 
        authorizationCode = "SPOTIFY_AUTHORIZATION_CODE"
    }
}.buildClient().token.access_token.let { println(it) }
```
