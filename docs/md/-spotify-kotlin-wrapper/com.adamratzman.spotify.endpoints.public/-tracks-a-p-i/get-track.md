[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [TracksAPI](index.md) / [getTrack](./get-track.md)

# getTrack

`fun getTrack(track: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Track`](../../com.adamratzman.spotify.utils/-track/index.md)`?>`

Get Spotify catalog information for a single track identified by its unique Spotify ID.

### Parameters

`track` - the spotify id or uri for the track.

`market` - Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)

**Return**
nullable Track. This behavior is *the same* as in `getTracks`

