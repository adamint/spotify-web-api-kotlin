[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientPlayerAPI](index.md) / [setRepeatMode](./set-repeat-mode.md)

# setRepeatMode

`fun setRepeatMode(state: `[`PlayerRepeatState`](-player-repeat-state/index.md)`, deviceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Set the repeat mode for the user’s playback. Options are repeat-track, repeat-context, and off.

### Parameters

`state` - mode to describe how to repeat in the current context

`deviceId` - the device to play on