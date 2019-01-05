[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientFollowingAPI](index.md) / [unfollowUser](./unfollow-user.md)

# unfollowUser

`fun unfollowUser(user: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Remove the current user as a follower of another user

### Parameters

`user` - The user to be unfollowed from

### Exceptions

`BadRequestException` - if [user](unfollow-user.md#com.adamratzman.spotify.endpoints.client.ClientFollowingAPI$unfollowUser(kotlin.String)/user) is not found