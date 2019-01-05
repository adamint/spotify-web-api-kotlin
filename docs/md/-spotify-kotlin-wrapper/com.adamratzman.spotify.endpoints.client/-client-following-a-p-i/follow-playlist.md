[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientFollowingAPI](index.md) / [followPlaylist](./follow-playlist.md)

# followPlaylist

`fun followPlaylist(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, followPublicly: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Add the current user as a follower of a playlist.

### Parameters

`playlist` - the spotify id or uri of the playlist. Any playlist can be followed, regardless of its
public/private status, as long as you know its playlist ID.

`followPublicly` - Defaults to true. If true the playlist will be included in user’s public playlists,
if false it will remain private. To be able to follow playlists privately, the user must have granted the playlist-modify-private scope.

### Exceptions

`BadRequestException` - if the playlist is not found