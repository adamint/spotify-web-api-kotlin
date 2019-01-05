[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientLibraryAPI](index.md) / [contains](./contains.md)

# contains

`fun contains(type: `[`LibraryType`](../-library-type/index.md)`, id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>`

Check if the [LibraryType](../-library-type/index.md) with id [id](contains.md#com.adamratzman.spotify.endpoints.client.ClientLibraryAPI$contains(com.adamratzman.spotify.endpoints.client.LibraryType, kotlin.String)/id) is already saved in the current Spotify user’s ‘Your Music’ library.

### Parameters

`type` - the type of object (album or track)

`id` - the spotify id or uri of the object

### Exceptions

`BadRequestException` - if [id](contains.md#com.adamratzman.spotify.endpoints.client.ClientLibraryAPI$contains(com.adamratzman.spotify.endpoints.client.LibraryType, kotlin.String)/id) is not found`fun contains(type: `[`LibraryType`](../-library-type/index.md)`, vararg ids: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>>`

Check if one or more of [LibraryType](../-library-type/index.md) is already saved in the current Spotify user’s ‘Your Music’ library.

### Parameters

`type` - the type of objects (album or track)

`ids` - the spotify ids or uris of the objects

### Exceptions

`BadRequestException` - if any of the provided ids is invalid