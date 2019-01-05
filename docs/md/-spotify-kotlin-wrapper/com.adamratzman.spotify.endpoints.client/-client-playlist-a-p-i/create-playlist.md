[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientPlaylistAPI](index.md) / [createPlaylist](./create-playlist.md)

# createPlaylist

`fun createPlaylist(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, description: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, public: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null, collaborative: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null, user: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = (api as SpotifyClientAPI).userId): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Playlist`](../../com.adamratzman.spotify.utils/-playlist/index.md)`>`

Create a playlist for a Spotify user. (The playlist will be empty until you add tracks.)

### Parameters

`user` - The user’s Spotify user ID.

`name` - The name for the new playlist, for example "Your Coolest Playlist" . This name does not need to be
unique; a user may have several playlists with the same name.

`description` -

`public` - Defaults to true . If true the playlist will be public, if false it will be private.
To be able to create private playlists, the user must have granted the playlist-modify-private scope.

`collaborative` - Defaults to false . If true the playlist will be collaborative. Note that to create a
collaborative playlist you must also set public to false . To create collaborative playlists you must have
granted playlist-modify-private and playlist-modify-public scopes.

**Return**
The created [Playlist](../../com.adamratzman.spotify.utils/-playlist/index.md) object with no tracks

