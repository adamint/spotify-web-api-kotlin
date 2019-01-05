[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [RecommendationSeed](./index.md)

# RecommendationSeed

`data class RecommendationSeed`

Seed from which the recommendation was constructed

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `RecommendationSeed(initialPoolSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, afterFilteringSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, afterRelinkingSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?, href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)`<br>Seed from which the recommendation was constructed |

### Properties

| Name | Summary |
|---|---|
| [afterFilteringSize](after-filtering-size.md) | `val afterFilteringSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The number of tracks available after min_* and max_* filters have been applied. |
| [afterRelinkingSize](after-relinking-size.md) | `val afterRelinkingSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?`<br>The number of tracks available after relinking for regional availability. |
| [href](href.md) | `val href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>A link to the full track or artist data for this seed. For tracks this will be a link to a Track Object. For artists a link to an Artist Object. For genre seeds, this value will be null. |
| [id](id.md) | `val id: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The id used to select this seed. This will be the same as the string used in the seed_artists , seed_tracks or seed_genres parameter. |
| [initialPoolSize](initial-pool-size.md) | `val initialPoolSize: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The number of recommended tracks available for this seed. |
| [type](type.md) | `val type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The entity type of this seed. One of artist , track or genre. |
