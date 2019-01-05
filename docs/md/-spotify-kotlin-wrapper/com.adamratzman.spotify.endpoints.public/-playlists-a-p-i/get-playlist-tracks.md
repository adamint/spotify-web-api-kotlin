[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [PlaylistsAPI](index.md) / [getPlaylistTracks](./get-playlist-tracks.md)

# getPlaylistTracks

`fun getPlaylistTracks(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`PlaylistTrack`](../../com.adamratzman.spotify.utils/-playlist-track/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`PlaylistTrack`](../../com.adamratzman.spotify.utils/-playlist-track/index.md)`>>`

Get full details of the tracks of a playlist owned by a Spotify user.

### Parameters

`playlist` - the spotify id or uri for the playlist.

`market` - Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)

`limit` - The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.

`offset` - The index of the first item to return. Default: 0. Use with limit to get the next set of items

### Exceptions

`BadRequestException` - if the playlist cannot be found