[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [AbstractPagingObject](./index.md)

# AbstractPagingObject

`abstract class AbstractPagingObject<T> : `[`ArrayList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/index.html)`<`[`T`](index.md#T)`>`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `AbstractPagingObject(href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, items: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](index.md#T)`>, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, next: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0, previous: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, total: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [href](href.md) | `val href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>A link to the Web API endpoint returning the full result of the request. |
| [itemClazz](item-clazz.md) | `lateinit var itemClazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<`[`T`](index.md#T)`>` |
| [items](items.md) | `val items: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](index.md#T)`>`<br>The requested data. |
| [limit](limit.md) | `val limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The maximum number of items in the response (as set in the query or by default). |
| [next](next.md) | `val next: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>URL to the next page of items. ( null if none) |
| [offset](offset.md) | `val offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The offset of the items returned (as set in the query or by default). |
| [previous](previous.md) | `val previous: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>URL to the previous page of items. ( null if none) |
| [total](total.md) | `val total: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The maximum number of items available to return. |

### Inheritors

| Name | Summary |
|---|---|
| [CursorBasedPagingObject](../-cursor-based-paging-object/index.md) | `class CursorBasedPagingObject<T> : `[`AbstractPagingObject`](./index.md)`<`[`T`](../-cursor-based-paging-object/index.md#T)`>`<br>The cursor-based paging object is a container for a set of objects. It contains a key called items (whose value is an array of the requested objects) along with other keys like next and cursors that can be useful in future calls. |
| [PagingObject](../-paging-object/index.md) | `class PagingObject<T> : `[`AbstractPagingObject`](./index.md)`<`[`T`](../-paging-object/index.md#T)`>`<br>The offset-based paging object is a container for a set of objects. It contains a key called items (whose value is an array of the requested objects) along with other keys like previous, next and limit that can be useful in future calls. |
