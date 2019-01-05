[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [BrowseAPI](index.md) / [getCategoryList](./get-category-list.md)

# getCategoryList

`fun getCategoryList(limit: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, offset: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, locale: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null): `[`SpotifyRestActionPaging`](../../com.adamratzman.spotify.main/-spotify-rest-action-paging/index.md)`<`[`SpotifyCategory`](../../com.adamratzman.spotify.utils/-spotify-category/index.md)`, `[`PagingObject`](../../com.adamratzman.spotify.utils/-paging-object/index.md)`<`[`SpotifyCategory`](../../com.adamratzman.spotify.utils/-spotify-category/index.md)`>>`

Get a list of categories used to tag items in Spotify (on, for example, the Spotify player’s “Browse” tab).

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

**Return**
Default category list if [locale](get-category-list.md#com.adamratzman.spotify.endpoints.public.BrowseAPI$getCategoryList(kotlin.Int, kotlin.Int, kotlin.String, com.adamratzman.spotify.utils.Market)/locale) is invalid, otherwise the localized PagingObject

