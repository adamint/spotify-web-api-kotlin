[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [PlaylistsAPI](index.md) / [getPlaylists](./get-playlists.md)

# getPlaylists

`fun getPlaylists(user: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`SimplePlaylist`](../../com.adamratzman.spotify.utils/-simple-playlist/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`SimplePlaylist`](../../com.adamratzman.spotify.utils/-simple-playlist/index.md)`>>`

Get a list of the playlists owned or followed by a Spotify user. Lookups for non-existant users return empty [PagingObject](../../com.adamratzman.spotify.utils/-paging-object/index.md)s
(blame Spotify)

### Parameters

`user` - The user’s Spotify user ID.

`limit` - The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.

`offset` - The index of the first item to return. Default: 0. Use with limit to get the next set of items

**Return**
[PagingObject](../../com.adamratzman.spotify.utils/-paging-object/index.md) of [SimplePlaylist](../../com.adamratzman.spotify.utils/-simple-playlist/index.md)s **ONLY if** the user can be found. Otherwise, an empty paging object is returned.
This does not have the detail of full [Playlist](../../com.adamratzman.spotify.utils/-playlist/index.md) objects.

