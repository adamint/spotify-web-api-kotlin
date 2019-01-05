[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [ArtistsAPI](index.md) / [getArtistTopTracks](./get-artist-top-tracks.md)

# getArtistTopTracks

`fun getArtistTopTracks(artist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)` = Market.US): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Track`](../../com.adamratzman.spotify.utils/-track/index.md)`>>`

Get Spotify catalog information about an artist’s top tracks **by country**. Contains only up to **10** tracks with no
[CursorBasedPagingObject](../../com.adamratzman.spotify.utils/-cursor-based-paging-object/index.md) to go between top track pages. Unfortunately, this isn't likely to change soon

### Parameters

`artist` - the spotify id or uri for the artist.

`market` - The country ([Market](../../com.adamratzman.spotify.utils/-market/index.md)) to search. Unlike endpoints with optional Track Relinking, the Market is **not** optional.

### Exceptions

`BadRequestException` - if tracks are not available in the specified [Market](../../com.adamratzman.spotify.utils/-market/index.md) or the [artist](get-artist-top-tracks.md#com.adamratzman.spotify.endpoints.public.ArtistsAPI$getArtistTopTracks(kotlin.String, com.adamratzman.spotify.utils.Market)/artist) is not found