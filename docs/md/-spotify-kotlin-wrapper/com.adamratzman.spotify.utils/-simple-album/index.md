[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [SimpleAlbum](./index.md)

# SimpleAlbum

`data class SimpleAlbum : `[`Linkable`](../-linkable/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SimpleAlbum(_albumType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, artists: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SimpleArtist`](../-simple-artist/index.md)`>, availableMarkets: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>? = null, externalUrls: `[`HashMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>, href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, images: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SpotifyImage`](../-spotify-image/index.md)`>, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, _uri: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, uri: `[`AlbumURI`](../-album-u-r-i/index.md)` = AlbumURI(_uri), releaseDate: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, releaseDatePrecision: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, totalTracks: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, albumGroupString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, restrictions: `[`Restrictions`](../-restrictions/index.md)`? = null, albumGroup: `[`AlbumResultType`](../-album-result-type/index.md)`? = albumGroupString?.let { _ ->
        AlbumResultType.values().find { it.id == albumGroupString }
    })` |

### Properties

| Name | Summary |
|---|---|
| [albumGroup](album-group.md) | `val albumGroup: `[`AlbumResultType`](../-album-result-type/index.md)`?`<br>Optional. The field is present when getting an artist’s albums. Possible values are “album”, “single”, “compilation”, “appears_on”. Compare to album_type this field represents relationship between the artist and the album. |
| [albumType](album-type.md) | `val albumType: `[`AlbumResultType`](../-album-result-type/index.md)<br>The type of the album: one of “album”, “single”, or “compilation”. |
| [artists](artists.md) | `val artists: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SimpleArtist`](../-simple-artist/index.md)`>`<br>The artists of the album. Each artist object includes a link in href to more detailed information about the artist. |
| [availableMarkets](available-markets.md) | `val availableMarkets: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?`<br>The markets in which the album is available: ISO 3166-1 alpha-2 country codes. Note that an album is considered available in a market when at least 1 of its tracks is available in that market. |
| [externalUrls](external-urls.md) | `val externalUrls: `[`HashMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>Known external URLs for this album. |
| [href](href.md) | `val href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>A link to the Web API endpoint providing full details of the album. |
| [id](id.md) | `val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The Spotify id for the album |
| [images](images.md) | `val images: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SpotifyImage`](../-spotify-image/index.md)`>`<br>The cover art for the album in various sizes, widest first. |
| [name](name.md) | `val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name of the album. In case of an album takedown, the value may be an empty string. |
| [releaseDate](release-date.md) | `val releaseDate: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The date the album was first released, for example 1981. Depending on the precision, it might be shown as 1981-12 or 1981-12-15. |
| [releaseDatePrecision](release-date-precision.md) | `val releaseDatePrecision: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The precision with which release_date value is known: year , month , or day. |
| [restrictions](restrictions.md) | `val restrictions: `[`Restrictions`](../-restrictions/index.md)`?`<br>Part of the response when Track Relinking is applied, the original track is not available in the given market, and Spotify did not have any tracks to relink it with. The track response will still contain metadata for the original track, and a restrictions object containing the reason why the track is not available: "restrictions" : {"reason" : "market"} |
| [totalTracks](total-tracks.md) | `val totalTracks: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [type](type.md) | `val type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The object type: “album” |
| [uri](uri.md) | `val uri: `[`AlbumURI`](../-album-u-r-i/index.md)<br>The Spotify URI for the album. |

### Inherited Properties

| Name | Summary |
|---|---|
| [api](../-linkable/api.md) | `lateinit var api: `[`SpotifyAPI`](../../com.adamratzman.spotify.main/-spotify-a-p-i/index.md) |

### Functions

| Name | Summary |
|---|---|
| [toFullAlbum](to-full-album.md) | `fun toFullAlbum(market: `[`Market`](../-market/index.md)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Album`](../-album/index.md)`?>` |
