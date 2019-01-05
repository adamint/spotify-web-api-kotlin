[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [ArtistsAPI](index.md) / [getArtists](./get-artists.md)

# getArtists

`fun getArtists(vararg artists: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Artist`](../../com.adamratzman.spotify.utils/-artist/index.md)`?>>`

Get Spotify catalog information for several artists based on their Spotify IDs. **Artists not found are returned as null inside the ordered list**

### Parameters

`artists` - the spotify ids or uris representing the artists.