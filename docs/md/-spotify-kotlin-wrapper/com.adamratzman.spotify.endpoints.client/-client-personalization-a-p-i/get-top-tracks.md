[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.client](../index.md) / [ClientPersonalizationAPI](index.md) / [getTopTracks](./get-top-tracks.md)

# getTopTracks

`fun getTopTracks(limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, timeRange: `[`TimeRange`](-time-range/index.md)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`Track`](../../com.adamratzman.spotify.utils/-track/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`Track`](../../com.adamratzman.spotify.utils/-track/index.md)`>>`

Get the current user’s top tracks based on calculated affinity.
Affinity is a measure of the expected preference a user has for a particular track or artist.  It is based on user
behavior, including play history, but does not include actions made while in incognito mode. Light or infrequent
users of Spotify may not have sufficient play history to generate a full affinity data set. As a user’s behavior
is likely to shift over time, this preference data is available over three time spans. See time_range in the
query parameter table for more information. For each time range, the top 50 tracks and artists are available
for each user. In the future, it is likely that this restriction will be relaxed. This data is typically updated
once each day for each user.

### Parameters

`limit` - The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.

`offset` - The index of the first item to return. Default: 0. Use with limit to get the next set of items

`timeRange` - the time range to which to compute this. The default is [TimeRange.MEDIUM_TERM](-time-range/-m-e-d-i-u-m_-t-e-r-m.md)

**Return**
[PagingObject](../../com.adamratzman.spotify.utils/-paging-object/index.md) of full [Track](../../com.adamratzman.spotify.utils/-track/index.md) objects sorted by affinity

