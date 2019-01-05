[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientFollowingAPI](index.md) / [isFollowingArtist](./is-following-artist.md)

# isFollowingArtist

`fun isFollowingArtist(artist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>`

Check to see if the current user is following a Spotify artist.

### Parameters

`artist` - artist id to check.

### Exceptions

`BadRequestException` - if [artist](is-following-artist.md#com.adamratzman.spotify.endpoints.client.ClientFollowingAPI$isFollowingArtist(kotlin.String)/artist) is a non-existing id