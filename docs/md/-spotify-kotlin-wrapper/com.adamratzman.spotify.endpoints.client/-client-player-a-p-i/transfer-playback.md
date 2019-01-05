[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientPlayerAPI](index.md) / [transferPlayback](./transfer-playback.md)

# transferPlayback

`fun transferPlayback(vararg deviceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, play: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Transfer playback to a new device and determine if it should start playing.

### Parameters

`deviceId` - the device to play on

`play` - whether to immediately start playback on the transferred device