[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.main](../index.md) / [SpotifyApiBuilderJava](./index.md)

# SpotifyApiBuilderJava

`class SpotifyApiBuilderJava`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SpotifyApiBuilderJava(clientId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, clientSecret: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [authorizationCode](authorization-code.md) | `var authorizationCode: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [clientId](client-id.md) | `val clientId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [clientSecret](client-secret.md) | `val clientSecret: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [redirectUri](redirect-uri.md) | `var redirectUri: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [token](token.md) | `var token: `[`Token`](../../com.adamratzman.spotify.utils/-token/index.md)`?` |
| [tokenString](token-string.md) | `var tokenString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [useCache](use-cache.md) | `var useCache: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Functions

| Name | Summary |
|---|---|
| [authorizationCode](authorization-code.md) | `fun authorizationCode(authorizationCode: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`SpotifyApiBuilderJava`](./index.md) |
| [buildClient](build-client.md) | `fun buildClient(automaticRefresh: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`SpotifyClientAPI`](../-spotify-client-a-p-i/index.md) |
| [buildCredentialed](build-credentialed.md) | `fun buildCredentialed(): `[`SpotifyAPI`](../-spotify-a-p-i/index.md) |
| [redirectUri](redirect-uri.md) | `fun redirectUri(redirectUri: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`SpotifyApiBuilderJava`](./index.md) |
| [token](token.md) | `fun token(token: `[`Token`](../../com.adamratzman.spotify.utils/-token/index.md)`?): `[`SpotifyApiBuilderJava`](./index.md) |
| [tokenString](token-string.md) | `fun tokenString(tokenString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`SpotifyApiBuilderJava`](./index.md) |
| [useCache](use-cache.md) | `fun useCache(useCache: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`SpotifyApiBuilderJava`](./index.md) |
