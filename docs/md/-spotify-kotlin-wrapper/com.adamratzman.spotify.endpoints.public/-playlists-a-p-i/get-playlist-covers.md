[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [PlaylistsAPI](index.md) / [getPlaylistCovers](./get-playlist-covers.md)

# getPlaylistCovers

`fun getPlaylistCovers(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SpotifyImage`](../../com.adamratzman.spotify.utils/-spotify-image/index.md)`>>`

Get the current image associated with a specific playlist.

### Parameters

`playlist` - the spotify id or uri for the playlist.

### Exceptions

`BadRequestException` - if the playlist cannot be found