[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientPlaylistAPI](index.md) / [setPlaylistTracks](./set-playlist-tracks.md)

# setPlaylistTracks

`fun setPlaylistTracks(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg tracks: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Replace all the tracks in a playlist, overwriting its existing tracks. This powerful request can be useful
for replacing tracks, re-ordering existing tracks, or clearing the playlist.

### Parameters

`playlist` - the spotify id or uri for the playlist.

`tracks` - The Spotify track ids.

### Exceptions

`BadRequestException` - if playlist is not found or illegal tracks are provided