[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [TracksAPI](index.md) / [getAudioAnalysis](./get-audio-analysis.md)

# getAudioAnalysis

`fun getAudioAnalysis(track: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`AudioAnalysis`](../../com.adamratzman.spotify.utils/-audio-analysis/index.md)`>`

Get a detailed audio analysis for a single track identified by its unique Spotify ID.

### Parameters

`track` - the spotify id or uri for the track.

### Exceptions

`BadRequestException` - if [track](get-audio-analysis.md#com.adamratzman.spotify.endpoints.public.TracksAPI$getAudioAnalysis(kotlin.String)/track) cannot be found