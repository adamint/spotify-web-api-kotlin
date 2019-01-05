[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [ArtistsAPI](index.md) / [getRelatedArtists](./get-related-artists.md)

# getRelatedArtists

`fun getRelatedArtists(artist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Artist`](../../com.adamratzman.spotify.utils/-artist/index.md)`>>`

Get Spotify catalog information about artists similar to a given artist.
Similarity is based on analysis of the Spotify community’s listening history.

### Parameters

`artist` - the spotify id or uri for the artist.

### Exceptions

`BadRequestException` - if the [artist](get-related-artists.md#com.adamratzman.spotify.endpoints.public.ArtistsAPI$getRelatedArtists(kotlin.String)/artist) is not found

**Return**
List of *never-null*, but possibly empty Artist objects representing similar artists

