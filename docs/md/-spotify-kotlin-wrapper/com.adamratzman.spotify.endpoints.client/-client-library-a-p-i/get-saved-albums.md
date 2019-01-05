[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientLibraryAPI](index.md) / [getSavedAlbums](./get-saved-albums.md)

# getSavedAlbums

`fun getSavedAlbums(limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`SavedAlbum`](../../com.adamratzman.spotify.utils/-saved-album/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`SavedAlbum`](../../com.adamratzman.spotify.utils/-saved-album/index.md)`>>`

Get a list of the albums saved in the current Spotify user’s ‘Your Music’ library.

### Parameters

`limit` - The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.

`offset` - The index of the first item to return. Default: 0. Use with limit to get the next set of items

`market` - Provide this parameter if you want the list of returned items to be relevant to a particular country.
If omitted, the returned items will be relevant to all countries.

**Return**
Paging Object of [SavedAlbum](../../com.adamratzman.spotify.utils/-saved-album/index.md) ordered by position in library

