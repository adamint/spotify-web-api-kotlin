[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [TuneableTrackAttribute](index.md) / [INSTRUMENTALNESS](./-i-n-s-t-r-u-m-e-n-t-a-l-n-e-s-s.md)

# INSTRUMENTALNESS

`INSTRUMENTALNESS`

Predicts whether a track contains no vocals. “Ooh” and “aah” sounds are treated as
instrumental in this context. Rap or spoken word tracks are clearly “vocal”. The
closer the instrumentalness value is to 1.0, the greater likelihood the track contains
no vocal content. Values above 0.5 are intended to represent instrumental tracks, but
confidence is higher as the value approaches 1.0.

### Inherited Functions

| Name | Summary |
|---|---|
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
