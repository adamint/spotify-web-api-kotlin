[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientPlaylistAPI](index.md) / [getClientPlaylists](./get-client-playlists.md)

# getClientPlaylists

`fun getClientPlaylists(limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`SimplePlaylist`](../../com.adamratzman.spotify.utils/-simple-playlist/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`SimplePlaylist`](../../com.adamratzman.spotify.utils/-simple-playlist/index.md)`>>`

Get a list of the playlists owned or followed by a Spotify user.

### Parameters

`limit` - The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.

`offset` - The index of the first item to return. Default: 0. Use with limit to get the next set of items

### Exceptions

`BadRequestException` - if the filters provided are illegal