[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [LinkedTrack](./index.md)

# LinkedTrack

`data class LinkedTrack`

Represents a [relinked track](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking). This is playable in the
searched market. If null, the API result is playable in the market.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `LinkedTrack(externalUrls: `[`HashMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, _uri: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, uri: `[`TrackURI`](../-track-u-r-i/index.md)` = TrackURI(_uri))`<br>Represents a [relinked track](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking). This is playable in the searched market. If null, the API result is playable in the market. |

### Properties

| Name | Summary |
|---|---|
| [externalUrls](external-urls.md) | `val externalUrls: `[`HashMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [href](href.md) | `val href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [id](id.md) | `val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [type](type.md) | `val type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [uri](uri.md) | `val uri: `[`TrackURI`](../-track-u-r-i/index.md) |
