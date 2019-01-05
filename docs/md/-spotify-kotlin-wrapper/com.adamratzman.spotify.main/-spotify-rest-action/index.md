[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.main](../index.md) / [SpotifyRestAction](./index.md)

# SpotifyRestAction

`open class SpotifyRestAction<T>`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SpotifyRestAction(api: `[`SpotifyAPI`](../-spotify-a-p-i/index.md)`, supplier: `[`Supplier`](http://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)`<`[`T`](index.md#T)`>)` |

### Properties

| Name | Summary |
|---|---|
| [api](api.md) | `val api: `[`SpotifyAPI`](../-spotify-a-p-i/index.md) |

### Functions

| Name | Summary |
|---|---|
| [asFuture](as-future.md) | `fun asFuture(): `[`CompletableFuture`](http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)`<`[`T`](index.md#T)`>` |
| [complete](complete.md) | `fun complete(): `[`T`](index.md#T) |
| [queue](queue.md) | `fun queue(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`fun queue(consumer: (`[`T`](index.md#T)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`fun queue(consumer: (`[`T`](index.md#T)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`, failure: (`[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [queueAfter](queue-after.md) | `fun queueAfter(quantity: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, timeUnit: `[`TimeUnit`](http://docs.oracle.com/javase/8/docs/api/java/util/concurrent/TimeUnit.html)` = TimeUnit.SECONDS, consumer: (`[`T`](index.md#T)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [toString](to-string.md) | `open fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [SpotifyRestActionPaging](../-spotify-rest-action-paging/index.md) | `class SpotifyRestActionPaging<Z, T : `[`AbstractPagingObject`](../../com.adamratzman.spotify.utils/-abstract-paging-object/index.md)`<`[`Z`](../-spotify-rest-action-paging/index.md#Z)`>> : `[`SpotifyRestAction`](./index.md)`<`[`T`](../-spotify-rest-action-paging/index.md#T)`>` |
