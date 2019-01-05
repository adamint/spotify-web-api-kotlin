[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [ArtistsAPI](./index.md)

# ArtistsAPI

`class ArtistsAPI : `[`SpotifyEndpoint`](../../com.adamratzman.spotify.utils/-spotify-endpoint/index.md)

Endpoints for retrieving information about one or more artists from the Spotify catalog.

### Types

| Name | Summary |
|---|---|
| [AlbumInclusionStrategy](-album-inclusion-strategy/index.md) | `enum class AlbumInclusionStrategy`<br>Describes object types to include when finding albums |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ArtistsAPI(api: `[`SpotifyAPI`](../../com.adamratzman.spotify.main/-spotify-a-p-i/index.md)`)`<br>Endpoints for retrieving information about one or more artists from the Spotify catalog. |

### Inherited Properties

| Name | Summary |
|---|---|
| [api](../../com.adamratzman.spotify.utils/-spotify-endpoint/api.md) | `val api: `[`SpotifyAPI`](../../com.adamratzman.spotify.main/-spotify-a-p-i/index.md) |

### Functions

| Name | Summary |
|---|---|
| [getArtist](get-artist.md) | `fun getArtist(artist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Artist`](../../com.adamratzman.spotify.utils/-artist/index.md)`?>`<br>Get Spotify catalog information for a single artist identified by their unique Spotify ID. |
| [getArtistAlbums](get-artist-albums.md) | `fun getArtistAlbums(artist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null, vararg include: `[`AlbumInclusionStrategy`](-album-inclusion-strategy/index.md)`): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`SimpleAlbum`](../../com.adamratzman.spotify.utils/-simple-album/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`SimpleAlbum`](../../com.adamratzman.spotify.utils/-simple-album/index.md)`>>`<br>Get Spotify catalog information about an artist’s albums. |
| [getArtistTopTracks](get-artist-top-tracks.md) | `fun getArtistTopTracks(artist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)` = Market.US): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Track`](../../com.adamratzman.spotify.utils/-track/index.md)`>>`<br>Get Spotify catalog information about an artist’s top tracks **by country**. Contains only up to **10** tracks with no [CursorBasedPagingObject](../../com.adamratzman.spotify.utils/-cursor-based-paging-object/index.md) to go between top track pages. Unfortunately, this isn't likely to change soon |
| [getArtists](get-artists.md) | `fun getArtists(vararg artists: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Artist`](../../com.adamratzman.spotify.utils/-artist/index.md)`?>>`<br>Get Spotify catalog information for several artists based on their Spotify IDs. **Artists not found are returned as null inside the ordered list** |
| [getRelatedArtists](get-related-artists.md) | `fun getRelatedArtists(artist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Artist`](../../com.adamratzman.spotify.utils/-artist/index.md)`>>`<br>Get Spotify catalog information about artists similar to a given artist. Similarity is based on analysis of the Spotify community’s listening history. |

### Inherited Functions

| Name | Summary |
|---|---|
| [toAction](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action.md) | `fun <T> toAction(supplier: `[`Supplier`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)`<`[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action.md#T)`>): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action.md#T)`>` |
| [toActionPaging](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md) | `fun <Z, T : `[`AbstractPagingObject`](../../com.adamratzman.spotify.utils/-abstract-paging-object/index.md)`<`[`Z`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#Z)`>> toActionPaging(supplier: `[`Supplier`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)`<`[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#T)`>): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`Z`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#Z)`, `[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#T)`>` |
