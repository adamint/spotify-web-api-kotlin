[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [TracksAPI](index.md) / [getAudioFeatures](./get-audio-features.md)

# getAudioFeatures

`fun getAudioFeatures(track: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`AudioFeatures`](../../com.adamratzman.spotify.utils/-audio-features/index.md)`>`

Get audio feature information for a single track identified by its unique Spotify ID.

### Parameters

`track` - the spotify id or uri for the track.

### Exceptions

`BadRequestException` - if [track](get-audio-features.md#com.adamratzman.spotify.endpoints.public.TracksAPI$getAudioFeatures(kotlin.String)/track) cannot be found`fun getAudioFeatures(vararg tracks: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`AudioFeatures`](../../com.adamratzman.spotify.utils/-audio-features/index.md)`?>>`

Get audio features for multiple tracks based on their Spotify IDs.

### Parameters

`tracks` - the spotify id or uri for the tracks.

**Return**
Ordered list of possibly-null [AudioFeatures](../../com.adamratzman.spotify.utils/-audio-features/index.md) objects.

