[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientFollowingAPI](index.md) / [isFollowingUsers](./is-following-users.md)

# isFollowingUsers

`fun isFollowingUsers(vararg users: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>>`

Check to see if the current user is following one or more other Spotify users.

### Parameters

`users` - List of the user Spotify IDs to check. Max 50

### Exceptions

`BadRequestException` - if [users](is-following-users.md#com.adamratzman.spotify.endpoints.client.ClientFollowingAPI$isFollowingUsers(kotlin.Array((kotlin.String)))/users) contains a non-existing id