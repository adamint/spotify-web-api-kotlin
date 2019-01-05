[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [Linkable](./index.md)

# Linkable

`abstract class Linkable`

Allow for track relinking

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Linkable()`<br>Allow for track relinking |

### Properties

| Name | Summary |
|---|---|
| [api](api.md) | `lateinit var api: `[`SpotifyAPI`](../../com.adamratzman.spotify.main/-spotify-a-p-i/index.md) |

### Inheritors

| Name | Summary |
|---|---|
| [RelinkingAvailableResponse](../-relinking-available-response/index.md) | `abstract class RelinkingAvailableResponse : `[`Linkable`](./index.md) |
| [SimpleAlbum](../-simple-album/index.md) | `data class SimpleAlbum : `[`Linkable`](./index.md) |
| [SimpleArtist](../-simple-artist/index.md) | `data class SimpleArtist : `[`Linkable`](./index.md) |
| [SimplePlaylist](../-simple-playlist/index.md) | `data class SimplePlaylist : `[`Linkable`](./index.md) |
