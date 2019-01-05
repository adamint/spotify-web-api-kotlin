[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientPlaylistAPI](index.md) / [reorderPlaylistTracks](./reorder-playlist-tracks.md)

# reorderPlaylistTracks

`fun reorderPlaylistTracks(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, reorderRangeStart: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, reorderRangeLength: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, insertionPoint: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, snapshotId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Snapshot`](-snapshot/index.md)`>`

Reorder a track or a group of tracks in a playlist.

When reordering tracks, the timestamp indicating when they were added and the user who added them will be kept
untouched. In addition, the users following the playlists won’t be notified about changes in the playlists
when the tracks are reordered.

### Parameters

`playlist` - the spotify id or uri for the playlist.

`reorderRangeStart` - The position of the first track to be reordered.

`reorderRangeLength` - The amount of tracks to be reordered. Defaults to 1 if not set.
The range of tracks to be reordered begins from the range_start position, and includes the range_length subsequent tracks.
Example: To move the tracks at index 9-10 to the start of the playlist, range_start is set to 9, and range_length is set to 2.

`insertionPoint` - The position where the tracks should be inserted. To reorder the tracks to the end of the playlist, simply set insert_before to the position after the last track.

`snapshotId` - the playlist snapshot against which to apply this action. **recommended to have**

### Exceptions

`BadRequestException` - if the playlist is not found or illegal filters are applied