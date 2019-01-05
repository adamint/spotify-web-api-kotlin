[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [BrowseAPI](index.md) / [getNewReleases](./get-new-releases.md)

# getNewReleases

`fun getNewReleases(limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`SimpleAlbum`](../../com.adamratzman.spotify.utils/-simple-album/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`SimpleAlbum`](../../com.adamratzman.spotify.utils/-simple-album/index.md)`>>`

Get a list of new album releases featured in Spotify (shown, for example, on a Spotify player’s “Browse” tab).

### Parameters

`limit` - The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.

`offset` - The index of the first item to return. Default: 0. Use with limit to get the next set of items

`market` - Provide this parameter if you want the list of returned items to be relevant to a particular country.
If omitted, the returned items will be relevant to all countries.

### Exceptions

`BadRequestException` - if filter parameters are illegal