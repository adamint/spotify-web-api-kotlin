[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [BrowseAPI](index.md) / [getRecommendations](./get-recommendations.md)

# getRecommendations

`fun getRecommendations(seedArtists: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>? = null, seedGenres: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>? = null, seedTracks: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>? = null, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null, targetAttributes: `[`HashMap`](http://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)`<`[`TuneableTrackAttribute`](../-tuneable-track-attribute/index.md)`, `[`Number`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-number/index.html)`> = hashMapOf(), minAttributes: `[`HashMap`](http://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)`<`[`TuneableTrackAttribute`](../-tuneable-track-attribute/index.md)`, `[`Number`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-number/index.html)`> = hashMapOf(), maxAttributes: `[`HashMap`](http://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)`<`[`TuneableTrackAttribute`](../-tuneable-track-attribute/index.md)`, `[`Number`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-number/index.html)`> = hashMapOf()): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`RecommendationResponse`](../../com.adamratzman.spotify.utils/-recommendation-response/index.md)`>`

Create a playlist-style listening experience based on seed artists, tracks and genres.
Recommendations are generated based on the available information for a given seed entity and matched against similar
artists and tracks. If there is sufficient information about the provided seeds, a list of tracks will be returned
together with pool size details. For artists and tracks that are very new or obscure there might not be enough data
to generate a list of tracks.

**5** seeds of any combination of [seedArtists](get-recommendations.md#com.adamratzman.spotify.endpoints.public.BrowseAPI$getRecommendations(kotlin.collections.List((kotlin.String)), kotlin.collections.List((kotlin.String)), kotlin.collections.List((kotlin.String)), kotlin.Int, com.adamratzman.spotify.utils.Market, java.util.HashMap((com.adamratzman.spotify.endpoints.public.TuneableTrackAttribute, kotlin.Number)), java.util.HashMap((com.adamratzman.spotify.endpoints.public.TuneableTrackAttribute, kotlin.Number)), java.util.HashMap((com.adamratzman.spotify.endpoints.public.TuneableTrackAttribute, kotlin.Number)))/seedArtists), [seedGenres](get-recommendations.md#com.adamratzman.spotify.endpoints.public.BrowseAPI$getRecommendations(kotlin.collections.List((kotlin.String)), kotlin.collections.List((kotlin.String)), kotlin.collections.List((kotlin.String)), kotlin.Int, com.adamratzman.spotify.utils.Market, java.util.HashMap((com.adamratzman.spotify.endpoints.public.TuneableTrackAttribute, kotlin.Number)), java.util.HashMap((com.adamratzman.spotify.endpoints.public.TuneableTrackAttribute, kotlin.Number)), java.util.HashMap((com.adamratzman.spotify.endpoints.public.TuneableTrackAttribute, kotlin.Number)))/seedGenres), and [seedTracks](get-recommendations.md#com.adamratzman.spotify.endpoints.public.BrowseAPI$getRecommendations(kotlin.collections.List((kotlin.String)), kotlin.collections.List((kotlin.String)), kotlin.collections.List((kotlin.String)), kotlin.Int, com.adamratzman.spotify.utils.Market, java.util.HashMap((com.adamratzman.spotify.endpoints.public.TuneableTrackAttribute, kotlin.Number)), java.util.HashMap((com.adamratzman.spotify.endpoints.public.TuneableTrackAttribute, kotlin.Number)), java.util.HashMap((com.adamratzman.spotify.endpoints.public.TuneableTrackAttribute, kotlin.Number)))/seedTracks) can be provided. AT LEAST 1 seed must be provided.

**All attributes** are weighted equally.

See [here](https://developer.spotify.com/documentation/web-api/reference/browse/get-recommendations/#tuneable-track-attributes) for a list
and descriptions of tuneable track attributes and their ranges.

### Parameters

`seedArtists` - A possibly null provided list of Artist IDs to be used to generate recommendations

`seedGenres` - A possibly null provided list of Genre IDs to be used to generate recommendations. Invalid genres are ignored

`seedTracks` - A possibly null provided list of Track IDs to be used to generate recommendations

`limit` - The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.

`market` - Provide this parameter if you want the list of returned items to be relevant to a particular country.
If omitted, the returned items will be relevant to all countries.

`targetAttributes` - For each of the tunable track attributes a target value may be provided.
Tracks with the attribute values nearest to the target values will be preferred.

`minAttributes` - For each tunable track attribute, a hard floor on the selected track attribute’s value can be provided.

`maxAttributes` - For each tunable track attribute, a hard ceiling on the selected track attribute’s value can be provided.
For example, setting max instrumentalness equal to 0.35 would filter out most tracks that are likely to be instrumental.

### Exceptions

`BadRequestException` - if any filter is applied illegally

**Return**
[RecommendationResponse](../../com.adamratzman.spotify.utils/-recommendation-response/index.md) with [RecommendationSeed](../../com.adamratzman.spotify.utils/-recommendation-seed/index.md)s used and [SimpleTrack](../../com.adamratzman.spotify.utils/-simple-track/index.md)s found

