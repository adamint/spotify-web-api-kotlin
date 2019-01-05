[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [AlbumAPI](index.md) / [getAlbumTracks](./get-album-tracks.md)

# getAlbumTracks

`fun getAlbumTracks(album: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`SimpleTrack`](../../com.adamratzman.spotify.utils/-simple-track/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`SimpleTrack`](../../com.adamratzman.spotify.utils/-simple-track/index.md)`>>`

Get Spotify catalog information about an album’s tracks. Optional parameters can be used to limit the number of tracks returned.

### Parameters

`album` - the spotify id or uri for the album.

`limit` - The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.

`offset` - The index of the first item to return. Default: 0. Use with limit to get the next set of items

`market` - Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)

### Exceptions

`BadRequestException` - if the [album](get-album-tracks.md#com.adamratzman.spotify.endpoints.public.AlbumAPI$getAlbumTracks(kotlin.String, kotlin.Int, kotlin.Int, com.adamratzman.spotify.utils.Market)/album) is not found, or positioning of [limit](get-album-tracks.md#com.adamratzman.spotify.endpoints.public.AlbumAPI$getAlbumTracks(kotlin.String, kotlin.Int, kotlin.Int, com.adamratzman.spotify.utils.Market)/limit) or [offset](get-album-tracks.md#com.adamratzman.spotify.endpoints.public.AlbumAPI$getAlbumTracks(kotlin.String, kotlin.Int, kotlin.Int, com.adamratzman.spotify.utils.Market)/offset) is illegal.