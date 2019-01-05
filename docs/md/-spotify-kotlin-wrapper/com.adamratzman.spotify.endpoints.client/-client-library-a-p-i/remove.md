[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientLibraryAPI](index.md) / [remove](./remove.md)

# remove

`fun remove(type: `[`LibraryType`](../-library-type/index.md)`, id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Remove one of [LibraryType](../-library-type/index.md) (track or album) from the current user’s ‘Your Music’ library.

### Parameters

`type` - the type of object to check against (album or track)

`id` - the spotify id or uri of the object

### Exceptions

`BadRequestException` - if any of the provided ids is invalid`fun remove(type: `[`LibraryType`](../-library-type/index.md)`, vararg ids: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Remove one or more of the [LibraryType](../-library-type/index.md) (tracks or albums) from the current user’s ‘Your Music’ library.

### Parameters

`type` - the type of objects to check against (album or track)

`ids` - the spotify ids or uris of the objects

### Exceptions

`BadRequestException` - if any of the provided ids is invalid