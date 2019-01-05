[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [PagingObject](./index.md)

# PagingObject

`class PagingObject<T> : `[`AbstractPagingObject`](../-abstract-paging-object/index.md)`<`[`T`](index.md#T)`>`

The offset-based paging object is a container for a set of objects. It contains a key called items
(whose value is an array of the requested objects) along with other keys like previous, next and
limit that can be useful in future calls.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PagingObject(href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, items: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](index.md#T)`>, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, next: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, previous: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, total: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)`<br>The offset-based paging object is a container for a set of objects. It contains a key called items (whose value is an array of the requested objects) along with other keys like previous, next and limit that can be useful in future calls. |

### Inherited Properties

| Name | Summary |
|---|---|
| [href](../-abstract-paging-object/href.md) | `val href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>A link to the Web API endpoint returning the full result of the request. |
| [itemClazz](../-abstract-paging-object/item-clazz.md) | `lateinit var itemClazz: `[`Class`](http://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)`<`[`T`](../-abstract-paging-object/index.md#T)`>` |
| [items](../-abstract-paging-object/items.md) | `val items: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](../-abstract-paging-object/index.md#T)`>`<br>The requested data. |
| [limit](../-abstract-paging-object/limit.md) | `val limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The maximum number of items in the response (as set in the query or by default). |
| [next](../-abstract-paging-object/next.md) | `val next: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>URL to the next page of items. ( null if none) |
| [offset](../-abstract-paging-object/offset.md) | `val offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The offset of the items returned (as set in the query or by default). |
| [previous](../-abstract-paging-object/previous.md) | `val previous: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>URL to the previous page of items. ( null if none) |
| [total](../-abstract-paging-object/total.md) | `val total: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The maximum number of items available to return. |

### Functions

| Name | Summary |
|---|---|
| [getAll](get-all.md) | `fun getAll(): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`PagingObject`](./index.md)`<`[`T`](index.md#T)`>>>`<br>Get all PagingObjects associated with the request |
| [getAllItems](get-all-items.md) | `fun getAllItems(): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](index.md#T)`>>`<br>Get all items of type [T](index.md#T) associated with the request |
| [getNext](get-next.md) | `fun getNext(): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`PagingObject`](./index.md)`<`[`T`](index.md#T)`>?>`<br>Get the next set of [T](index.md#T) items |
| [getPrevious](get-previous.md) | `fun getPrevious(): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`PagingObject`](./index.md)`<`[`T`](index.md#T)`>?>`<br>Get the previous set of [T](index.md#T) items |
