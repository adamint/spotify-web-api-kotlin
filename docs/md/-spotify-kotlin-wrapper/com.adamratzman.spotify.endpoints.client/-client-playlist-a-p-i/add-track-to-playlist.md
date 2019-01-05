[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientPlaylistAPI](index.md) / [addTrackToPlaylist](./add-track-to-playlist.md)

# addTrackToPlaylist

`fun addTrackToPlaylist(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, track: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, position: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Add a track to a user’s playlist.

### Parameters

`playlist` - the spotify id or uri for the playlist.

`track` - track id or uri

`position` - The position to insert the tracks, a zero-based index. For example, to insert the tracks in the
first position: position=0; to insert the tracks in the third position: position=2 . If omitted, the tracks will
be appended to the playlist. Tracks are added in the order they are listed in the query string or request body.

### Exceptions

`BadRequestException` - if any invalid track ids is provided or the playlist is not found