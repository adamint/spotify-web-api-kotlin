[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientFollowingAPI](index.md) / [unfollowPlaylist](./unfollow-playlist.md)

# unfollowPlaylist

`fun unfollowPlaylist(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Remove the current user as a follower of a playlist.

### Parameters

`playlist` - the spotify id or uri of the playlist that is to be no longer followed.

### Exceptions

`BadRequestException` - if the playlist is not found