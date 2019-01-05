[SpotifyKotlinWrapper](../index.md) / [com.adamratzman.spotify.main](./index.md)

## Package com.adamratzman.spotify.main

### Types

| Name | Summary |
|---|---|
| [SpotifyAPI](-spotify-a-p-i/index.md) | `abstract class SpotifyAPI` |
| [SpotifyApiBuilder](-spotify-api-builder/index.md) | `class SpotifyApiBuilder` |
| [SpotifyApiBuilderJava](-spotify-api-builder-java/index.md) | `class SpotifyApiBuilderJava` |
| [SpotifyAppAPI](-spotify-app-a-p-i/index.md) | `class SpotifyAppAPI : `[`SpotifyAPI`](-spotify-a-p-i/index.md) |
| [SpotifyClientAPI](-spotify-client-a-p-i/index.md) | `class SpotifyClientAPI : `[`SpotifyAPI`](-spotify-a-p-i/index.md) |
| [SpotifyCredentials](-spotify-credentials/index.md) | `data class SpotifyCredentials` |
| [SpotifyCredentialsBuilder](-spotify-credentials-builder/index.md) | `class SpotifyCredentialsBuilder` |
| [SpotifyLogger](-spotify-logger/index.md) | `class SpotifyLogger` |
| [SpotifyRestAction](-spotify-rest-action/index.md) | `open class SpotifyRestAction<T>` |
| [SpotifyRestActionPaging](-spotify-rest-action-paging/index.md) | `class SpotifyRestActionPaging<Z, T : `[`AbstractPagingObject`](../com.adamratzman.spotify.utils/-abstract-paging-object/index.md)`<`[`Z`](-spotify-rest-action-paging/index.md#Z)`>> : `[`SpotifyRestAction`](-spotify-rest-action/index.md)`<`[`T`](-spotify-rest-action-paging/index.md#T)`>` |
| [SpotifyScope](-spotify-scope/index.md) | `enum class SpotifyScope` |
| [SpotifyUserAuthorizationBuilder](-spotify-user-authorization-builder/index.md) | `class SpotifyUserAuthorizationBuilder`<br>Authentication methods. |

### Exceptions

| Name | Summary |
|---|---|
| [SpotifyException](-spotify-exception/index.md) | `class SpotifyException : `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html) |

### Functions

| Name | Summary |
|---|---|
| [spotifyApi](spotify-api.md) | `fun spotifyApi(block: `[`SpotifyApiBuilder`](-spotify-api-builder/index.md)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`SpotifyApiBuilder`](-spotify-api-builder/index.md) |
