[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientPlayerAPI](index.md) / [startPlayback](./start-playback.md)

# startPlayback

`fun startPlayback(album: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, artist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, playlist: `[`PlaylistURI`](../../com.adamratzman.spotify.utils/-playlist-u-r-i/index.md)`? = null, offsetNum: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offsetTrackId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, deviceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, vararg tracksToPlay: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Start or resume playback.
**Note:** Only one of the following can be used: [album](start-playback.md#com.adamratzman.spotify.endpoints.client.ClientPlayerAPI$startPlayback(kotlin.String, kotlin.String, com.adamratzman.spotify.utils.PlaylistURI, kotlin.Int, kotlin.String, kotlin.String, kotlin.Array((kotlin.String)))/album), [artist](start-playback.md#com.adamratzman.spotify.endpoints.client.ClientPlayerAPI$startPlayback(kotlin.String, kotlin.String, com.adamratzman.spotify.utils.PlaylistURI, kotlin.Int, kotlin.String, kotlin.String, kotlin.Array((kotlin.String)))/artist), [playlist](start-playback.md#com.adamratzman.spotify.endpoints.client.ClientPlayerAPI$startPlayback(kotlin.String, kotlin.String, com.adamratzman.spotify.utils.PlaylistURI, kotlin.Int, kotlin.String, kotlin.String, kotlin.Array((kotlin.String)))/playlist), or [tracksToPlay](start-playback.md#com.adamratzman.spotify.endpoints.client.ClientPlayerAPI$startPlayback(kotlin.String, kotlin.String, com.adamratzman.spotify.utils.PlaylistURI, kotlin.Int, kotlin.String, kotlin.String, kotlin.Array((kotlin.String)))/tracksToPlay). Else, you will
not see expected results.

**Note also:** You can only use one of the following: [offsetNum](start-playback.md#com.adamratzman.spotify.endpoints.client.ClientPlayerAPI$startPlayback(kotlin.String, kotlin.String, com.adamratzman.spotify.utils.PlaylistURI, kotlin.Int, kotlin.String, kotlin.String, kotlin.Array((kotlin.String)))/offsetNum) or [offsetTrackId](start-playback.md#com.adamratzman.spotify.endpoints.client.ClientPlayerAPI$startPlayback(kotlin.String, kotlin.String, com.adamratzman.spotify.utils.PlaylistURI, kotlin.Int, kotlin.String, kotlin.String, kotlin.Array((kotlin.String)))/offsetTrackId)

**Specify nothing to play to simply resume playback**

### Parameters

`album` - an album id or uri to play

`artist` - an artist id or uri for whom to play

`playlist` - a playlist id or uri from which to play

`tracksToPlay` - track ids or uris to play. these are converted into URIs. Max 100

`offsetNum` - Indicates from where in the context playback should start. Only available with use of [album](start-playback.md#com.adamratzman.spotify.endpoints.client.ClientPlayerAPI$startPlayback(kotlin.String, kotlin.String, com.adamratzman.spotify.utils.PlaylistURI, kotlin.Int, kotlin.String, kotlin.String, kotlin.Array((kotlin.String)))/album) or [playlist](start-playback.md#com.adamratzman.spotify.endpoints.client.ClientPlayerAPI$startPlayback(kotlin.String, kotlin.String, com.adamratzman.spotify.utils.PlaylistURI, kotlin.Int, kotlin.String, kotlin.String, kotlin.Array((kotlin.String)))/playlist)
or when [tracksToPlay](start-playback.md#com.adamratzman.spotify.endpoints.client.ClientPlayerAPI$startPlayback(kotlin.String, kotlin.String, com.adamratzman.spotify.utils.PlaylistURI, kotlin.Int, kotlin.String, kotlin.String, kotlin.Array((kotlin.String)))/tracksToPlay) is used.

`offsetTrackId` - Does the same as [offsetNum](start-playback.md#com.adamratzman.spotify.endpoints.client.ClientPlayerAPI$startPlayback(kotlin.String, kotlin.String, com.adamratzman.spotify.utils.PlaylistURI, kotlin.Int, kotlin.String, kotlin.String, kotlin.Array((kotlin.String)))/offsetNum) but with a track id or uri instead of place number

`deviceId` - the device to play on

### Exceptions

`BadRequestException` - if more than one type of play type is specified or the offset is illegal.