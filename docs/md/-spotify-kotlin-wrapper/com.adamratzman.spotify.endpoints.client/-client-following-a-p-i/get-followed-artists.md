[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientFollowingAPI](index.md) / [getFollowedArtists](./get-followed-artists.md)

# getFollowedArtists

`fun getFollowedArtists(limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, after: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`Artist`](../../com.adamratzman.spotify.utils/-artist/index.md)`, `[`CursorBasedPagingObject`](../../com.adamratzman.spotify.utils/-cursor-based-paging-object/index.md)`<`[`Artist`](../../com.adamratzman.spotify.utils/-artist/index.md)`>>`

Get the current user’s followed artists.

**Return**
[CursorBasedPagingObject](../../com.adamratzman.spotify.utils/-cursor-based-paging-object/index.md) ([Information about them](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#the-benefits-of-linkedresults-pagingobjects-and-cursor-based-paging-objects)
with full [Artist](../../com.adamratzman.spotify.utils/-artist/index.md) objects

