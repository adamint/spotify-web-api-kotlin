[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [PagingObject](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`PagingObject(href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, items: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](index.md#T)`>, limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, next: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, previous: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, total: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)`

The offset-based paging object is a container for a set of objects. It contains a key called items
(whose value is an array of the requested objects) along with other keys like previous, next and
limit that can be useful in future calls.

