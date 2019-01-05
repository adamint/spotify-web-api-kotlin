[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientFollowingAPI](index.md) / [isFollowingPlaylist](./is-following-playlist.md)

# isFollowingPlaylist

`fun isFollowingPlaylist(playlistOwner: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, playlistId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>`

Check to see if the logged-in Spotify user is following the specified playlist.

### Parameters

`playlistOwner` - id or uri of the creator of the playlist

`playlistId` - playlist id or uri

### Exceptions

`BadRequestException` - if the playlist is not found

**Return**
booleans representing whether the user follows the playlist. User IDs **not** found will return false

