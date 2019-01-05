[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.main](../index.md) / [SpotifyAPI](./index.md)

# SpotifyAPI

`abstract class SpotifyAPI`

### Properties

| Name | Summary |
|---|---|
| [albums](albums.md) | `abstract val albums: `[`AlbumAPI`](../../com.adamratzman.spotify.endpoints.public/-album-a-p-i/index.md) |
| [artists](artists.md) | `abstract val artists: `[`ArtistsAPI`](../../com.adamratzman.spotify.endpoints.public/-artists-a-p-i/index.md) |
| [browse](browse.md) | `abstract val browse: `[`BrowseAPI`](../../com.adamratzman.spotify.endpoints.public/-browse-a-p-i/index.md) |
| [clientId](client-id.md) | `val clientId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [clientSecret](client-secret.md) | `val clientSecret: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [following](following.md) | `abstract val following: `[`FollowingAPI`](../../com.adamratzman.spotify.endpoints.public/-following-a-p-i/index.md) |
| [klaxon](klaxon.md) | `abstract val klaxon: Klaxon` |
| [playlists](playlists.md) | `abstract val playlists: `[`PlaylistsAPI`](../../com.adamratzman.spotify.endpoints.public/-playlists-a-p-i/index.md) |
| [search](search.md) | `abstract val search: `[`SearchAPI`](../../com.adamratzman.spotify.endpoints.public/-search-a-p-i/index.md) |
| [token](token.md) | `var token: `[`Token`](../../com.adamratzman.spotify.utils/-token/index.md) |
| [tracks](tracks.md) | `abstract val tracks: `[`TracksAPI`](../../com.adamratzman.spotify.endpoints.public/-tracks-a-p-i/index.md) |
| [useCache](use-cache.md) | `var useCache: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [users](users.md) | `abstract val users: `[`UserAPI`](../../com.adamratzman.spotify.endpoints.public/-user-a-p-i/index.md) |

### Functions

| Name | Summary |
|---|---|
| [clearCache](clear-cache.md) | `abstract fun clearCache(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getAuthorizationUrl](get-authorization-url.md) | `fun getAuthorizationUrl(vararg scopes: `[`SpotifyScope`](../-spotify-scope/index.md)`, redirectUri: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [refreshToken](refresh-token.md) | `abstract fun refreshToken(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [useLogger](use-logger.md) | `fun useLogger(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [SpotifyAppAPI](../-spotify-app-a-p-i/index.md) | `class SpotifyAppAPI : `[`SpotifyAPI`](./index.md) |
| [SpotifyClientAPI](../-spotify-client-a-p-i/index.md) | `class SpotifyClientAPI : `[`SpotifyAPI`](./index.md) |
