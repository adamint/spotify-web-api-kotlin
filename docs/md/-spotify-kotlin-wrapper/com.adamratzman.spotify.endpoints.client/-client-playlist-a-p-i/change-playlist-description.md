[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientPlaylistAPI](index.md) / [changePlaylistDescription](./change-playlist-description.md)

# changePlaylistDescription

`fun changePlaylistDescription(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, public: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null, collaborative: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null, description: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Change a playlist’s name and public/private state. (The user must, of course, own the playlist.)

### Parameters

`playlist` - the spotify id or uri for the playlist.

`name` - Optional. The name to change the playlist to.

`public` - Optional. Whether to make the playlist public or not.

`collaborative` - Optional. Whether to make the playlist collaborative or not.

`description` - Optional. Whether to change the description or not.

### Exceptions

`BadRequestException` - if the playlist is not found or parameters exceed the max length