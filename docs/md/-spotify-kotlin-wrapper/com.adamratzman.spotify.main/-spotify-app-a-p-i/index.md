[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.main](../index.md) / [SpotifyAppAPI](./index.md)

# SpotifyAppAPI

`class SpotifyAppAPI : `[`SpotifyAPI`](../-spotify-a-p-i/index.md)

### Properties

| Name | Summary |
|---|---|
| [albums](albums.md) | `val albums: `[`AlbumAPI`](../../com.adamratzman.spotify.endpoints.public/-album-a-p-i/index.md) |
| [artists](artists.md) | `val artists: `[`ArtistsAPI`](../../com.adamratzman.spotify.endpoints.public/-artists-a-p-i/index.md) |
| [browse](browse.md) | `val browse: `[`BrowseAPI`](../../com.adamratzman.spotify.endpoints.public/-browse-a-p-i/index.md) |
| [following](following.md) | `val following: `[`FollowingAPI`](../../com.adamratzman.spotify.endpoints.public/-following-a-p-i/index.md) |
| [klaxon](klaxon.md) | `val klaxon: Klaxon` |
| [playlists](playlists.md) | `val playlists: `[`PlaylistsAPI`](../../com.adamratzman.spotify.endpoints.public/-playlists-a-p-i/index.md) |
| [search](search.md) | `val search: `[`SearchAPI`](../../com.adamratzman.spotify.endpoints.public/-search-a-p-i/index.md) |
| [tracks](tracks.md) | `val tracks: `[`TracksAPI`](../../com.adamratzman.spotify.endpoints.public/-tracks-a-p-i/index.md) |
| [users](users.md) | `val users: `[`UserAPI`](../../com.adamratzman.spotify.endpoints.public/-user-a-p-i/index.md) |

### Inherited Properties

| Name | Summary |
|---|---|
| [clientId](../-spotify-a-p-i/client-id.md) | `val clientId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [clientSecret](../-spotify-a-p-i/client-secret.md) | `val clientSecret: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [token](../-spotify-a-p-i/token.md) | `var token: `[`Token`](../../com.adamratzman.spotify.utils/-token/index.md) |
| [useCache](../-spotify-a-p-i/use-cache.md) | `var useCache: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Functions

| Name | Summary |
|---|---|
| [clearCache](clear-cache.md) | `fun clearCache(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [refreshToken](refresh-token.md) | `fun refreshToken(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inherited Functions

| Name | Summary |
|---|---|
| [getAuthorizationUrl](../-spotify-a-p-i/get-authorization-url.md) | `fun getAuthorizationUrl(vararg scopes: `[`SpotifyScope`](../-spotify-scope/index.md)`, redirectUri: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [useLogger](../-spotify-a-p-i/use-logger.md) | `fun useLogger(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
