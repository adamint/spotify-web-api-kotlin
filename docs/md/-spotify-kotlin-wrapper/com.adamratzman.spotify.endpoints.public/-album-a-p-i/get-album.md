[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [AlbumAPI](index.md) / [getAlbum](./get-album.md)

# getAlbum

`fun getAlbum(album: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Album`](../../com.adamratzman.spotify.utils/-album/index.md)`?>`

Get Spotify catalog information for a single album.

### Parameters

`album` - the spotify id or uri for the album.

`market` - Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)

**Return**
full [Album](../../com.adamratzman.spotify.utils/-album/index.md) object if the provided id is found, otherwise null

