[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.main](../index.md) / [SpotifyApiBuilder](./index.md)

# SpotifyApiBuilder

`class SpotifyApiBuilder`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SpotifyApiBuilder()` |

### Properties

| Name | Summary |
|---|---|
| [useCache](use-cache.md) | `var useCache: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Functions

| Name | Summary |
|---|---|
| [authentication](authentication.md) | `fun authentication(block: `[`SpotifyUserAuthorizationBuilder`](../-spotify-user-authorization-builder/index.md)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Allows you to authenticate a [SpotifyClientAPI](../-spotify-client-a-p-i/index.md) with an authorization code or build [SpotifyAPI](../-spotify-a-p-i/index.md) using a refresh token |
| [buildClient](build-client.md) | `fun buildClient(automaticRefresh: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`SpotifyClientAPI`](../-spotify-client-a-p-i/index.md) |
| [buildClientAsync](build-client-async.md) | `fun buildClientAsync(consumer: (`[`SpotifyClientAPI`](../-spotify-client-a-p-i/index.md)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`, automaticRefresh: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [buildCredentialed](build-credentialed.md) | `fun buildCredentialed(): `[`SpotifyAPI`](../-spotify-a-p-i/index.md) |
| [buildCredentialedAsync](build-credentialed-async.md) | `fun buildCredentialedAsync(consumer: (`[`SpotifyAPI`](../-spotify-a-p-i/index.md)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [credentials](credentials.md) | `fun credentials(block: `[`SpotifyCredentialsBuilder`](../-spotify-credentials-builder/index.md)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getAuthorizationUrl](get-authorization-url.md) | `fun getAuthorizationUrl(vararg scopes: `[`SpotifyScope`](../-spotify-scope/index.md)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
