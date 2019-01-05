[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [TuneableTrackAttribute](index.md) / [POPULARITY](./-p-o-p-u-l-a-r-i-t-y.md)

# POPULARITY

`POPULARITY`

The popularity of the track. The value will be between 0 and 100, with 100 being the most popular.
The popularity is calculated by algorithm and is based, in the most part, on the total number of
plays the track has had and how recent those plays are. Note: When applying track relinking via
the market parameter, it is expected to find relinked tracks with popularities that do not match
min_*, max_*and target* popularities. These relinked tracks are accurate replacements for unplayable tracks with the expected popularity scores. Original, non-relinked tracks are available via the linked_from attribute of the relinked track response.

### Inherited Functions

| Name | Summary |
|---|---|
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
