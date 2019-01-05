[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [Token](./index.md)

# Token

`data class Token`

Represents a Spotify Token, retrieved through instantiating a new [SpotifyAPI](#)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Token(accessToken: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, tokenType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, expiresIn: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, refreshToken: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, scopeString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, scopes: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SpotifyScope`](../../com.adamratzman.spotify.main/-spotify-scope/index.md)`>? = scopeString?.let { str ->
        str.split(" ").mapNotNull { scope -> SpotifyScope.values().find { it.uri == scope } }
    })`<br>Represents a Spotify Token, retrieved through instantiating a new [SpotifyAPI](#) |

### Properties

| Name | Summary |
|---|---|
| [accessToken](access-token.md) | `val accessToken: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [expiresIn](expires-in.md) | `val expiresIn: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [refreshToken](refresh-token.md) | `val refreshToken: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [scopes](scopes.md) | `val scopes: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SpotifyScope`](../../com.adamratzman.spotify.main/-spotify-scope/index.md)`>?` |
| [tokenType](token-type.md) | `val tokenType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
