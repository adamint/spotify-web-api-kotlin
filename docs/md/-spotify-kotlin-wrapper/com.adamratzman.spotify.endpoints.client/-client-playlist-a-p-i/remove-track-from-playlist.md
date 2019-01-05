[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientPlaylistAPI](index.md) / [removeTrackFromPlaylist](./remove-track-from-playlist.md)

# removeTrackFromPlaylist

`fun removeTrackFromPlaylist(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, track: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, positions: `[`SpotifyTrackPositions`](../-spotify-track-positions/index.md)`, snapshotId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Snapshot`](-snapshot/index.md)`>`

Remove a track in the specified positions (zero-based) from the specified playlist.

### Parameters

`playlist` - the playlist id

`track` - the track id

`positions` - the positions at which the track is located in the playlist

`snapshotId` - the playlist snapshot against which to apply this action. **recommended to have**`fun removeTrackFromPlaylist(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, track: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, snapshotId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Snapshot`](-snapshot/index.md)`>`

Remove all occurrences of a track from the specified playlist.

### Parameters

`playlist` - the playlist id

`track` - the track id

`snapshotId` - the playlist snapshot against which to apply this action. **recommended to have**