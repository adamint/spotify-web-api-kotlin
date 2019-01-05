[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientFollowingAPI](index.md) / [unfollowUsers](./unfollow-users.md)

# unfollowUsers

`fun unfollowUsers(vararg users: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Remove the current user as a follower of other users

### Parameters

`users` - The users to be unfollowed from

### Exceptions

`BadRequestException` - if an invalid id is provided