[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientPlayerAPI](index.md) / [seek](./seek.md)

# seek

`fun seek(positionMs: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, deviceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Seeks to the given position in the user’s currently playing track.

### Parameters

`positionMs` - The position in milliseconds to seek to. Must be a positive number. Passing in a position
that is greater than the length of the track will cause the player to start playing the next song.

`deviceId` - the device to play on