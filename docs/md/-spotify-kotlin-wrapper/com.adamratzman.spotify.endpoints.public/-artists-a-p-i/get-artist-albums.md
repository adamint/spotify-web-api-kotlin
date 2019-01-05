[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [ArtistsAPI](index.md) / [getArtistAlbums](./get-artist-albums.md)

# getArtistAlbums

`fun getArtistAlbums(artist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null, vararg include: `[`AlbumInclusionStrategy`](-album-inclusion-strategy/index.md)`): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`SimpleAlbum`](../../com.adamratzman.spotify.utils/-simple-album/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`SimpleAlbum`](../../com.adamratzman.spotify.utils/-simple-album/index.md)`>>`

Get Spotify catalog information about an artist’s albums.

### Parameters

`artist` - artist id or uri

`market` - Supply this parameter to limit the response to one particular geographical market.

`limit` - The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.

`offset` - The index of the first item to return. Default: 0. Use with limit to get the next set of items

`include` - List of keywords that will be used to filter the response. If not supplied, all album groups will be returned.

### Exceptions

`BadRequestException` - if [artist](get-artist-albums.md#com.adamratzman.spotify.endpoints.public.ArtistsAPI$getArtistAlbums(kotlin.String, kotlin.Int, kotlin.Int, com.adamratzman.spotify.utils.Market, kotlin.Array((com.adamratzman.spotify.endpoints.public.ArtistsAPI.AlbumInclusionStrategy)))/artist) is not found, or filter parameters are illegal