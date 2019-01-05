[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [PlaylistsAPI](./index.md)

# PlaylistsAPI

`open class PlaylistsAPI : `[`SpotifyEndpoint`](../../com.adamratzman.spotify.utils/-spotify-endpoint/index.md)

Endpoints for retrieving information about a user’s playlists

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PlaylistsAPI(api: `[`SpotifyAPI`](../../com.adamratzman.spotify.main/-spotify-a-p-i/index.md)`)`<br>Endpoints for retrieving information about a user’s playlists |

### Inherited Properties

| Name | Summary |
|---|---|
| [api](../../com.adamratzman.spotify.utils/-spotify-endpoint/api.md) | `val api: `[`SpotifyAPI`](../../com.adamratzman.spotify.main/-spotify-a-p-i/index.md) |

### Functions

| Name | Summary |
|---|---|
| [getPlaylist](get-playlist.md) | `fun getPlaylist(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Playlist`](../../com.adamratzman.spotify.utils/-playlist/index.md)`?>`<br>Get a playlist owned by a Spotify user. |
| [getPlaylistCovers](get-playlist-covers.md) | `fun getPlaylistCovers(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SpotifyImage`](../../com.adamratzman.spotify.utils/-spotify-image/index.md)`>>`<br>Get the current image associated with a specific playlist. |
| [getPlaylistTracks](get-playlist-tracks.md) | `fun getPlaylistTracks(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`PlaylistTrack`](../../com.adamratzman.spotify.utils/-playlist-track/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`PlaylistTrack`](../../com.adamratzman.spotify.utils/-playlist-track/index.md)`>>`<br>Get full details of the tracks of a playlist owned by a Spotify user. |
| [getPlaylists](get-playlists.md) | `fun getPlaylists(user: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`SimplePlaylist`](../../com.adamratzman.spotify.utils/-simple-playlist/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`SimplePlaylist`](../../com.adamratzman.spotify.utils/-simple-playlist/index.md)`>>`<br>Get a list of the playlists owned or followed by a Spotify user. Lookups for non-existant users return empty [PagingObject](../../com.adamratzman.spotify.utils/-paging-object/index.md)s (blame Spotify) |

### Inherited Functions

| Name | Summary |
|---|---|
| [toAction](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action.md) | `fun <T> toAction(supplier: `[`Supplier`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)`<`[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action.md#T)`>): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action.md#T)`>` |
| [toActionPaging](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md) | `fun <Z, T : `[`AbstractPagingObject`](../../com.adamratzman.spotify.utils/-abstract-paging-object/index.md)`<`[`Z`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#Z)`>> toActionPaging(supplier: `[`Supplier`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)`<`[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#T)`>): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`Z`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#Z)`, `[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#T)`>` |

### Inheritors

| Name | Summary |
|---|---|
| [ClientPlaylistAPI](../../com.adamratzman.spotify.endpoints.client/-client-playlist-a-p-i/index.md) | `class ClientPlaylistAPI : `[`PlaylistsAPI`](./index.md)<br>Endpoints for retrieving information about a user’s playlists and for managing a user’s playlists. |
