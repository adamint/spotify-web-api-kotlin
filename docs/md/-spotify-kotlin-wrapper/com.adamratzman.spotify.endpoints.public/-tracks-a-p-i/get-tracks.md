[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [TracksAPI](index.md) / [getTracks](./get-tracks.md)

# getTracks

`fun getTracks(vararg tracks: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Track`](../../com.adamratzman.spotify.utils/-track/index.md)`?>>`

Get Spotify catalog information for multiple tracks based on their Spotify IDs.

### Parameters

`tracks` - the spotify id or uri for the tracks.

`market` - Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)

**Return**
List of possibly-null full [Track](../../com.adamratzman.spotify.utils/-track/index.md) objects.

