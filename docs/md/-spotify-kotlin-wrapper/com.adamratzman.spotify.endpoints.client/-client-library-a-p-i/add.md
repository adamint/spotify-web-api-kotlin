[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientLibraryAPI](index.md) / [add](./add.md)

# add

`fun add(type: `[`LibraryType`](../-library-type/index.md)`, id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Save one of [LibraryType](../-library-type/index.md) to the current user’s ‘Your Music’ library.

### Parameters

`type` - the type of object (album or track)

`id` - the spotify id or uri of the object

### Exceptions

`BadRequestException` - if the id is invalid`fun add(type: `[`LibraryType`](../-library-type/index.md)`, vararg ids: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`>`

Save one or more of [LibraryType](../-library-type/index.md) to the current user’s ‘Your Music’ library.

### Parameters

`type` - the type of objects to check against (album or track)

`ids` - the spotify ids or uris of the objects

### Exceptions

`BadRequestException` - if any of the provided ids is invalid