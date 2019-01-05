[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [PlaylistsAPI](index.md) / [getPlaylist](./get-playlist.md)

# getPlaylist

`fun getPlaylist(playlist: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Playlist`](../../com.adamratzman.spotify.utils/-playlist/index.md)`?>`

Get a playlist owned by a Spotify user.

### Parameters

`playlist` - the spotify id or uri for the playlist.

`market` - Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)

### Exceptions

`BadRequestException` - if the playlist is not found