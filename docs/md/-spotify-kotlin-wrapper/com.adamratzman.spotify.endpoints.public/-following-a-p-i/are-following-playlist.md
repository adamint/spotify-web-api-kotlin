[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [FollowingAPI](index.md) / [areFollowingPlaylist](./are-following-playlist.md)

# areFollowingPlaylist

`fun areFollowingPlaylist(playlistOwner: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg users: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>>`

Check to see if one or more Spotify users are following a specified playlist.

### Parameters

`playlistOwner` - id or uri of the creator of the playlist

`playlist` - playlist id or uri

`users` - user ids or uris to check

### Exceptions

`BadRequestException` - if the playlist is not found

**Return**
List of Booleans representing whether the user follows the playlist. User IDs **not** found will return false

