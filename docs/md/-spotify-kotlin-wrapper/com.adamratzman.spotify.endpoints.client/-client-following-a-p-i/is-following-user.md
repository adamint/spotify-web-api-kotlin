[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientFollowingAPI](index.md) / [isFollowingUser](./is-following-user.md)

# isFollowingUser

`fun isFollowingUser(user: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>`

Check to see if the current user is following another Spotify users.

### Parameters

`user` - user id or uri to check.

### Exceptions

`BadRequestException` - if [user](is-following-user.md#com.adamratzman.spotify.endpoints.client.ClientFollowingAPI$isFollowingUser(kotlin.String)/user) is a non-existing id