[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientFollowingAPI](index.md) / [unfollowArtists](./unfollow-artists.md)

# unfollowArtists

`fun unfollowArtists(vararg artists: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Remove the current user as a follower of artists

### Parameters

`artists` - The artists to be unfollowed from

### Exceptions

`BadRequestException` - if an invalid id is provided