[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [SearchAPI](./index.md)

# SearchAPI

`class SearchAPI : `[`SpotifyEndpoint`](../../com.adamratzman.spotify.utils/-spotify-endpoint/index.md)

Get Spotify catalog information about artists, albums, tracks or playlists that match a keyword string.

### Types

| Name | Summary |
|---|---|
| [SearchType](-search-type/index.md) | `enum class SearchType`<br>Describes which object to search for |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SearchAPI(api: `[`SpotifyAPI`](../../com.adamratzman.spotify.main/-spotify-a-p-i/index.md)`)`<br>Get Spotify catalog information about artists, albums, tracks or playlists that match a keyword string. |

### Inherited Properties

| Name | Summary |
|---|---|
| [api](../../com.adamratzman.spotify.utils/-spotify-endpoint/api.md) | `val api: `[`SpotifyAPI`](../../com.adamratzman.spotify.main/-spotify-a-p-i/index.md) |

### Functions

| Name | Summary |
|---|---|
| [searchAlbum](search-album.md) | `fun searchAlbum(query: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`SimpleAlbum`](../../com.adamratzman.spotify.utils/-simple-album/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`SimpleAlbum`](../../com.adamratzman.spotify.utils/-simple-album/index.md)`>>`<br>Get Spotify Catalog information about albums that match the keyword string. |
| [searchArtist](search-artist.md) | `fun searchArtist(query: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`Artist`](../../com.adamratzman.spotify.utils/-artist/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`Artist`](../../com.adamratzman.spotify.utils/-artist/index.md)`>>`<br>Get Spotify Catalog information about artists that match the keyword string. |
| [searchPlaylist](search-playlist.md) | `fun searchPlaylist(query: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`SimplePlaylist`](../../com.adamratzman.spotify.utils/-simple-playlist/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`SimplePlaylist`](../../com.adamratzman.spotify.utils/-simple-playlist/index.md)`>>`<br>Get Spotify Catalog information about playlists that match the keyword string. |
| [searchTrack](search-track.md) | `fun searchTrack(query: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`Track`](../../com.adamratzman.spotify.utils/-track/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`Track`](../../com.adamratzman.spotify.utils/-track/index.md)`>>`<br>Get Spotify Catalog information about tracks that match the keyword string. |

### Inherited Functions

| Name | Summary |
|---|---|
| [toAction](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action.md) | `fun <T> toAction(supplier: `[`Supplier`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)`<`[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action.md#T)`>): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action.md#T)`>` |
| [toActionPaging](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md) | `fun <Z, T : `[`AbstractPagingObject`](../../com.adamratzman.spotify.utils/-abstract-paging-object/index.md)`<`[`Z`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#Z)`>> toActionPaging(supplier: `[`Supplier`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)`<`[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#T)`>): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`Z`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#Z)`, `[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#T)`>` |
