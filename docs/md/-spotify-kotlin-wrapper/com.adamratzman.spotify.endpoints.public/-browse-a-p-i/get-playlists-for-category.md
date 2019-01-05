[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [BrowseAPI](index.md) / [getPlaylistsForCategory](./get-playlists-for-category.md)

# getPlaylistsForCategory

`fun getPlaylistsForCategory(categoryId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`SimplePlaylist`](../../com.adamratzman.spotify.utils/-simple-playlist/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`SimplePlaylist`](../../com.adamratzman.spotify.utils/-simple-playlist/index.md)`>>`

Get a list of Spotify playlists tagged with a particular category.

### Parameters

`market` - Provide this parameter if you want the list of returned items to be relevant to a particular country.
If omitted, the returned items will be relevant to all countries.

`limit` - The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.

`offset` - The index of the first item to return. Default: 0. Use with limit to get the next set of items

### Exceptions

`BadRequestException` - if [categoryId](get-playlists-for-category.md#com.adamratzman.spotify.endpoints.public.BrowseAPI$getPlaylistsForCategory(kotlin.String, kotlin.Int, kotlin.Int, com.adamratzman.spotify.utils.Market)/categoryId) is not found or filters are illegal