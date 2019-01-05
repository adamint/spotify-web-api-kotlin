[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [PlaylistTrack](./index.md)

# PlaylistTrack

`data class PlaylistTrack`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PlaylistTrack(primaryColor: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, addedAt: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, addedBy: `[`SpotifyPublicUser`](../-spotify-public-user/index.md)`?, isLocal: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?, track: `[`Track`](../-track/index.md)`, videoThumbnail: `[`VideoThumbnail`](../-video-thumbnail/index.md)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [addedAt](added-at.md) | `val addedAt: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>The date and time the track was added. Note that some very old playlists may return null in this field. |
| [addedBy](added-by.md) | `val addedBy: `[`SpotifyPublicUser`](../-spotify-public-user/index.md)`?`<br>The Spotify user who added the track. Note that some very old playlists may return null in this field. |
| [isLocal](is-local.md) | `val isLocal: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?`<br>Whether this track is a local file or not. |
| [primaryColor](primary-color.md) | `val primaryColor: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>Unknown. Spotify has released no information about this |
| [track](track.md) | `val track: `[`Track`](../-track/index.md)<br>Information about the track. |
| [videoThumbnail](video-thumbnail.md) | `val videoThumbnail: `[`VideoThumbnail`](../-video-thumbnail/index.md)`?` |
