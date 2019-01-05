[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientPlaylistAPI](index.md) / [removeTracksFromPlaylist](./remove-tracks-from-playlist.md)

# removeTracksFromPlaylist

`fun removeTracksFromPlaylist(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg tracks: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, snapshotId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Snapshot`](-snapshot/index.md)`>`

Remove all occurrences of the specified tracks from the given playlist.

### Parameters

`playlist` - the playlist id

`tracks` - an array of track ids

`snapshotId` - the playlist snapshot against which to apply this action. **recommended to have**`fun removeTracksFromPlaylist(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, vararg tracks: `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`SpotifyTrackPositions`](../-spotify-track-positions/index.md)`>, snapshotId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Snapshot`](-snapshot/index.md)`>`

Remove tracks (each with their own positions) from the given playlist.

### Parameters

`playlist` - the playlist id

`tracks` - an array of [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)s of track ids *and* track positions (zero-based)

`snapshotId` - the playlist snapshot against which to apply this action. **recommended to have**