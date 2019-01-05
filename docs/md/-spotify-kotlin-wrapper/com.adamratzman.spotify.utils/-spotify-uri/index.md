[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [SpotifyUri](./index.md)

# SpotifyUri

`sealed class SpotifyUri`

Represents a Spotify URI, parsed from either a Spotify ID or taken from an endpoint.

### Properties

| Name | Summary |
|---|---|
| [id](id.md) | `val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>representation of this uri as an id |
| [uri](uri.md) | `val uri: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>retrieve this URI as a string |

### Inheritors

| Name | Summary |
|---|---|
| [AlbumURI](../-album-u-r-i/index.md) | `class AlbumURI : `[`SpotifyUri`](./index.md)<br>Represents a Spotify **Album** URI, parsed from either a Spotify ID or taken from an endpoint. |
| [ArtistURI](../-artist-u-r-i/index.md) | `class ArtistURI : `[`SpotifyUri`](./index.md)<br>Represents a Spotify **Artist** URI, parsed from either a Spotify ID or taken from an endpoint. |
| [PlaylistURI](../-playlist-u-r-i/index.md) | `class PlaylistURI : `[`SpotifyUri`](./index.md)<br>Represents a Spotify **Playlist** URI, parsed from either a Spotify ID or taken from an endpoint. |
| [TrackURI](../-track-u-r-i/index.md) | `class TrackURI : `[`SpotifyUri`](./index.md)<br>Represents a Spotify **Track** URI, parsed from either a Spotify ID or taken from an endpoint. |
| [UserURI](../-user-u-r-i/index.md) | `class UserURI : `[`SpotifyUri`](./index.md)<br>Represents a Spotify **User** URI, parsed from either a Spotify ID or taken from an endpoint. |
