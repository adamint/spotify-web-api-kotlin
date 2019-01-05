[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [FollowingAPI](index.md) / [isFollowingPlaylist](./is-following-playlist.md)

# isFollowingPlaylist

`fun isFollowingPlaylist(playlistOwner: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, user: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>`

Check to see if a specific Spotify user is following the specified playlist.

### Parameters

`playlistOwner` - id or uri of the creator of the playlist

`playlist` - playlist id or uri

`user` - Spotify user id

### Exceptions

`BadRequestException` - if the playlist is not found

**Return**
booleans representing whether the user follows the playlist. User IDs **not** found will return false

