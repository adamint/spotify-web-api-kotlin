[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.utils](../index.md) / [AudioFeatures](index.md) / [instrumentalness](./instrumentalness.md)

# instrumentalness

`val instrumentalness: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)

Predicts whether a track contains no vocals. “Ooh” and “aah” sounds are
treated as instrumental in this context. Rap or spoken word tracks are clearly “vocal”. The closer
the instrumentalness value is to 1.0, the greater likelihood the track contains no vocal content.
Values above 0.5 are intended to represent instrumental tracks, but confidence is higher as
the value approaches 1.0.

### Property

`instrumentalness` - Predicts whether a track contains no vocals. “Ooh” and “aah” sounds are
treated as instrumental in this context. Rap or spoken word tracks are clearly “vocal”. The closer
the instrumentalness value is to 1.0, the greater likelihood the track contains no vocal content.
Values above 0.5 are intended to represent instrumental tracks, but confidence is higher as
the value approaches 1.0.