[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [Followers](./index.md)

# Followers

`data class Followers`

Spotify user's followers

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Followers(href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, total: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)`<br>Spotify user's followers |

### Properties

| Name | Summary |
|---|---|
| [href](href.md) | `val href: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>Will always be null, per the Spotify documentation, until the Web API is updated to support this. |
| [total](total.md) | `val total: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>-1 if the user object does not contain followers, otherwise the amount of followers the user has |
