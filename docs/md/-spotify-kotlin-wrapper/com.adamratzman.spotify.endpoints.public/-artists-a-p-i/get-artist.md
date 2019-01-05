[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [ArtistsAPI](index.md) / [getArtist](./get-artist.md)

# getArtist

`fun getArtist(artist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Artist`](../../com.adamratzman.spotify.utils/-artist/index.md)`?>`

Get Spotify catalog information for a single artist identified by their unique Spotify ID.

### Parameters

`artist` - the spotify id or uri for the artist.

**Return**
[Artist](../../com.adamratzman.spotify.utils/-artist/index.md) if valid artist id is provided, otherwise null

