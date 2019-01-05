[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [RelinkingAvailableResponse](./index.md)

# RelinkingAvailableResponse

`abstract class RelinkingAvailableResponse : `[`Linkable`](../-linkable/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `RelinkingAvailableResponse(linkedTrack: `[`LinkedTrack`](../-linked-track/index.md)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [linkedTrack](linked-track.md) | `val linkedTrack: `[`LinkedTrack`](../-linked-track/index.md)`?` |

### Inherited Properties

| Name | Summary |
|---|---|
| [api](../-linkable/api.md) | `lateinit var api: `[`SpotifyAPI`](../../com.adamratzman.spotify.main/-spotify-a-p-i/index.md) |

### Functions

| Name | Summary |
|---|---|
| [isRelinked](is-relinked.md) | `fun isRelinked(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [SimpleTrack](../-simple-track/index.md) | `data class SimpleTrack : `[`RelinkingAvailableResponse`](./index.md) |
| [Track](../-track/index.md) | `data class Track : `[`RelinkingAvailableResponse`](./index.md) |
