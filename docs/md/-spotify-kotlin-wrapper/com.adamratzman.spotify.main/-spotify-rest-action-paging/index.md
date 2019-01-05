[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.main](../index.md) / [SpotifyRestActionPaging](./index.md)

# SpotifyRestActionPaging

`class SpotifyRestActionPaging<Z, T : `[`AbstractPagingObject`](../../com.adamratzman.spotify.utils/-abstract-paging-object/index.md)`<`[`Z`](index.md#Z)`>> : `[`SpotifyRestAction`](../-spotify-rest-action/index.md)`<`[`T`](index.md#T)`>`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SpotifyRestActionPaging(api: `[`SpotifyAPI`](../-spotify-a-p-i/index.md)`, supplier: `[`Supplier`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)`<`[`T`](index.md#T)`>)` |

### Inherited Properties

| Name | Summary |
|---|---|
| [api](../-spotify-rest-action/api.md) | `val api: `[`SpotifyAPI`](../-spotify-a-p-i/index.md) |

### Functions

| Name | Summary |
|---|---|
| [getAll](get-all.md) | `fun getAll(): `[`SpotifyRestAction`](../-spotify-rest-action/index.md)`<`[`Sequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/-sequence/index.html)`<`[`AbstractPagingObject`](../../com.adamratzman.spotify.utils/-abstract-paging-object/index.md)`<`[`Z`](index.md#Z)`>>>` |
| [getAllItems](get-all-items.md) | `fun getAllItems(): `[`SpotifyRestAction`](../-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Z`](index.md#Z)`>>` |

### Inherited Functions

| Name | Summary |
|---|---|
| [asFuture](../-spotify-rest-action/as-future.md) | `fun asFuture(): `[`CompletableFuture`](http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)`<`[`T`](../-spotify-rest-action/index.md#T)`>` |
| [complete](../-spotify-rest-action/complete.md) | `fun complete(): `[`T`](../-spotify-rest-action/index.md#T) |
| [queue](../-spotify-rest-action/queue.md) | `fun queue(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`fun queue(consumer: (`[`T`](../-spotify-rest-action/index.md#T)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`fun queue(consumer: (`[`T`](../-spotify-rest-action/index.md#T)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`, failure: (`[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [queueAfter](../-spotify-rest-action/queue-after.md) | `fun queueAfter(quantity: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, timeUnit: `[`TimeUnit`](http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/TimeUnit.html)` = TimeUnit.SECONDS, consumer: (`[`T`](../-spotify-rest-action/index.md#T)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [toString](../-spotify-rest-action/to-string.md) | `open fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
