[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [BrowseAPI](index.md) / [getFeaturedPlaylists](./get-featured-playlists.md)

# getFeaturedPlaylists

`fun getFeaturedPlaylists(limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, locale: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null, timestamp: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`FeaturedPlaylists`](../../com.adamratzman.spotify.utils/-featured-playlists/index.md)`>`

Get a list of Spotify featured playlists (shown, for example, on a Spotify player’s ‘Browse’ tab).

### Parameters

`limit` - The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.

`offset` - The index of the first item to return. Default: 0. Use with limit to get the next set of items

`locale` - The desired language, consisting of a lowercase ISO 639-1 language code and an uppercase ISO 3166-1 alpha-2 country code, joined by an underscore. For example: es_MX, meaning “Spanish (Mexico)”.
Provide this parameter if you want the results returned in a particular language (where available).
Note that, if locale is not supplied, or if the specified language is not available,
all strings will be returned in the Spotify default language (American English. The locale parameter, combined with the country parameter, may give odd results if not carefully matched.
For example country=SE&locale=de_DE will return a list of categories relevant to Sweden but as German language strings.

`market` - Provide this parameter if you want the list of returned items to be relevant to a particular country.
If omitted, the returned items will be relevant to all countries.

`timestamp` - Use this parameter (time in milliseconds) to specify the user’s local time to get results tailored for that specific
date and time in the day. If not provided, the response defaults to the current UTC time.

### Exceptions

`BadRequestException` - if filter parameters are illegal or [locale](get-featured-playlists.md#com.adamratzman.spotify.endpoints.public.BrowseAPI$getFeaturedPlaylists(kotlin.Int, kotlin.Int, kotlin.String, com.adamratzman.spotify.utils.Market, kotlin.Long)/locale) does not exist