[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [TracksAPI](./index.md)

# TracksAPI

`class TracksAPI : `[`SpotifyEndpoint`](../../com.adamratzman.spotify.utils/-spotify-endpoint/index.md)

Endpoints for retrieving information about one or more tracks from the Spotify catalog.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `TracksAPI(api: `[`SpotifyAPI`](../../com.adamratzman.spotify.main/-spotify-a-p-i/index.md)`)`<br>Endpoints for retrieving information about one or more tracks from the Spotify catalog. |

### Inherited Properties

| Name | Summary |
|---|---|
| [api](../../com.adamratzman.spotify.utils/-spotify-endpoint/api.md) | `val api: `[`SpotifyAPI`](../../com.adamratzman.spotify.main/-spotify-a-p-i/index.md) |

### Functions

| Name | Summary |
|---|---|
| [getAudioAnalysis](get-audio-analysis.md) | `fun getAudioAnalysis(track: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`AudioAnalysis`](../../com.adamratzman.spotify.utils/-audio-analysis/index.md)`>`<br>Get a detailed audio analysis for a single track identified by its unique Spotify ID. |
| [getAudioFeatures](get-audio-features.md) | `fun getAudioFeatures(track: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`AudioFeatures`](../../com.adamratzman.spotify.utils/-audio-features/index.md)`>`<br>Get audio feature information for a single track identified by its unique Spotify ID.`fun getAudioFeatures(vararg tracks: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`AudioFeatures`](../../com.adamratzman.spotify.utils/-audio-features/index.md)`?>>`<br>Get audio features for multiple tracks based on their Spotify IDs. |
| [getTrack](get-track.md) | `fun getTrack(track: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`Track`](../../com.adamratzman.spotify.utils/-track/index.md)`?>`<br>Get Spotify catalog information for a single track identified by its unique Spotify ID. |
| [getTracks](get-tracks.md) | `fun getTracks(vararg tracks: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Track`](../../com.adamratzman.spotify.utils/-track/index.md)`?>>`<br>Get Spotify catalog information for multiple tracks based on their Spotify IDs. |

### Inherited Functions

| Name | Summary |
|---|---|
| [toAction](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action.md) | `fun <T> toAction(supplier: `[`Supplier`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)`<`[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action.md#T)`>): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action.md#T)`>` |
| [toActionPaging](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md) | `fun <Z, T : `[`AbstractPagingObject`](../../com.adamratzman.spotify.utils/-abstract-paging-object/index.md)`<`[`Z`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#Z)`>> toActionPaging(supplier: `[`Supplier`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)`<`[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#T)`>): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`Z`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#Z)`, `[`T`](../../com.adamratzman.spotify.utils/-spotify-endpoint/to-action-paging.md#T)`>` |
