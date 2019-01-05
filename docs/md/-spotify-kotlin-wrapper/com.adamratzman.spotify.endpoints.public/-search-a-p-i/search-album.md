[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [SearchAPI](index.md) / [searchAlbum](./search-album.md)

# searchAlbum

`fun searchAlbum(query: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`SimpleAlbum`](../../com.adamratzman.spotify.utils/-simple-album/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`SimpleAlbum`](../../com.adamratzman.spotify.utils/-simple-album/index.md)`>>`

Get Spotify Catalog information about albums that match the keyword string.

### Parameters

`query` - Search query keywords and optional field filters and operators.

`market` - Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)

`limit` - The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.

`offset` - The index of the first item to return. Default: 0. Use with limit to get the next set of items

### Exceptions

`BadRequestException` - if filters are illegal or query is malformed

**Return**
[PagingObject](../../com.adamratzman.spotify.utils/-paging-object/index.md) of non-full [SimpleAlbum](../../com.adamratzman.spotify.utils/-simple-album/index.md) objects ordered by likelihood of correct match

