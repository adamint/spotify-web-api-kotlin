[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientFollowingAPI](index.md) / [isFollowingArtists](./is-following-artists.md)

# isFollowingArtists

`fun isFollowingArtists(vararg artists: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>>`

Check to see if the current user is following one or more artists.

### Parameters

`artists` - List of the artist ids or uris to check. Max 50

### Exceptions

`BadRequestException` - if [artists](is-following-artists.md#com.adamratzman.spotify.endpoints.client.ClientFollowingAPI$isFollowingArtists(kotlin.Array((kotlin.String)))/artists) contains a non-existing id