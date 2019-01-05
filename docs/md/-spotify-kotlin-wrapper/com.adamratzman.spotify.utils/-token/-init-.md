[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [Token](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Token(accessToken: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, tokenType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, expiresIn: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, refreshToken: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, scopeString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, scopes: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SpotifyScope`](../../com.adamratzman.spotify.main/-spotify-scope/index.md)`>? = scopeString?.let { str ->
        str.split(" ").mapNotNull { scope -> SpotifyScope.values().find { it.uri == scope } }
    })`

Represents a Spotify Token, retrieved through instantiating a new [SpotifyAPI](#)

