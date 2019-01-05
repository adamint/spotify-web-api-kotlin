[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.main](../index.md) / [SpotifyUserAuthorizationBuilder](./index.md)

# SpotifyUserAuthorizationBuilder

`class SpotifyUserAuthorizationBuilder`

Authentication methods.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SpotifyUserAuthorizationBuilder(authorizationCode: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, tokenString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, token: `[`Token`](../../com.adamratzman.spotify.utils/-token/index.md)`? = null)`<br>Authentication methods. |

### Properties

| Name | Summary |
|---|---|
| [authorizationCode](authorization-code.md) | `var authorizationCode: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>Only available when building [SpotifyClientAPI](../-spotify-client-a-p-i/index.md). Spotify auth code |
| [token](token.md) | `var token: `[`Token`](../../com.adamratzman.spotify.utils/-token/index.md)`?`<br>Build the API using an existing token. If you're building [SpotifyClientAPI](../-spotify-client-a-p-i/index.md), this will be your **access** token. If you're building [SpotifyAPI](../-spotify-a-p-i/index.md), it will be your **refresh** token |
| [tokenString](token-string.md) | `var tokenString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>Build the API using an existing token (string). If you're building [SpotifyClientAPI](../-spotify-client-a-p-i/index.md), this will be your **access** token. If you're building [SpotifyAPI](../-spotify-a-p-i/index.md), it will be your **refresh** token. There is a *very* limited time constraint on these before the API automatically refreshes them |
