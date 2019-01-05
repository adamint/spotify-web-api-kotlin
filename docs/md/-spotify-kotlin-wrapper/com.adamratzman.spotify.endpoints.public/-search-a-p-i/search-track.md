[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [SearchAPI](index.md) / [searchTrack](./search-track.md)

# searchTrack

`fun searchTrack(query: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`Track`](../../com.adamratzman.spotify.utils/-track/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`Track`](../../com.adamratzman.spotify.utils/-track/index.md)`>>`

Get Spotify Catalog information about tracks that match the keyword string.

### Parameters

`query` - Search query keywords and optional field filters and operators.

`market` - Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)

`limit` - The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.

`offset` - The index of the first item to return. Default: 0. Use with limit to get the next set of items

### Exceptions

`BadRequestException` - if filters are illegal or query is malformed

**Return**
[PagingObject](../../com.adamratzman.spotify.utils/-paging-object/index.md) of non-full [SimpleTrack](../../com.adamratzman.spotify.utils/-simple-track/index.md) objects ordered by likelihood of correct match

