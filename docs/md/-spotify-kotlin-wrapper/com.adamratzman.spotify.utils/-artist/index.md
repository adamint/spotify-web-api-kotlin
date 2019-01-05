[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [Artist](./index.md)

# Artist

`data class Artist`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Artist(externalUrls: `[`HashMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, followers: `[`Followers`](../-followers/index.md)`, genres: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, images: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SpotifyImage`](../-spotify-image/index.md)`>, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, popularity: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, _uri: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, uri: `[`ArtistURI`](../-artist-u-r-i/index.md)` = ArtistURI(_uri))` |

### Properties

| Name | Summary |
|---|---|
| [externalUrls](external-urls.md) | `val externalUrls: `[`HashMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>Known external URLs for this artist. |
| [followers](followers.md) | `val followers: `[`Followers`](../-followers/index.md)<br>Information about the followers of the artist. |
| [genres](genres.md) | `val genres: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>A list of the genres the artist is associated with. For example: "Prog Rock" , "Post-Grunge". (If not yet classified, the array is empty.) |
| [href](href.md) | `val href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>A link to the Web API endpoint providing full details of the artist. |
| [id](id.md) | `val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The Spotify ID for the artist. |
| [images](images.md) | `val images: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SpotifyImage`](../-spotify-image/index.md)`>`<br>Images of the artist in various sizes, widest first. |
| [name](name.md) | `val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name of the artist |
| [popularity](popularity.md) | `val popularity: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The popularity of the artist. The value will be between 0 and 100, with 100 being the most popular. The artist’s popularity is calculated from the popularity of all the artist’s tracks. |
| [type](type.md) | `val type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The object type: "artist" |
| [uri](uri.md) | `val uri: `[`ArtistURI`](../-artist-u-r-i/index.md)<br>The Spotify URI for the artist. |
