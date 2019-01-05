[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [AlbumAPI](index.md) / [getAlbums](./get-albums.md)

# getAlbums

`fun getAlbums(vararg albums: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Album`](../../com.adamratzman.spotify.utils/-album/index.md)`?>>`

Get Spotify catalog information for multiple albums identified by their Spotify IDs. **Albums not found are returned as null inside the ordered list**

### Parameters

`albums` - the spotify ids or uris for the albums.

`market` - Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)