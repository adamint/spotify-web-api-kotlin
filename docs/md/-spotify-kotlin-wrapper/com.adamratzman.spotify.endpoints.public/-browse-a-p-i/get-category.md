[SpotifyKotlinWrapper](../../index.md) / [com.adamratzman.spotify.endpoints.public](../index.md) / [BrowseAPI](index.md) / [getCategory](./get-category.md)

# getCategory

`fun getCategory(categoryId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, market: `[`Market`](../../com.adamratzman.spotify.utils/-market/index.md)`? = null, locale: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`SpotifyRestAction`](../../com.adamratzman.spotify.main/-spotify-rest-action/index.md)`<`[`SpotifyCategory`](../../com.adamratzman.spotify.utils/-spotify-category/index.md)`>`

Get a single category used to tag items in Spotify (on, for example, the Spotify player’s “Browse” tab).

### Parameters

`locale` - The desired language, consisting of a lowercase ISO 639-1 language code and an uppercase ISO 3166-1 alpha-2 country code, joined by an underscore. For example: es_MX, meaning “Spanish (Mexico)”.
Provide this parameter if you want the results returned in a particular language (where available).
Note that, if locale is not supplied, or if the specified language is not available,
all strings will be returned in the Spotify default language (American English. The locale parameter, combined with the country parameter, may give odd results if not carefully matched.
For example country=SE&locale=de_DE will return a list of categories relevant to Sweden but as German language strings.

`market` - Provide this parameter if you want the list of returned items to be relevant to a particular country.
If omitted, the returned items will be relevant to all countries.

### Exceptions

`BadRequestException` - if [categoryId](get-category.md#com.adamratzman.spotify.endpoints.public.BrowseAPI$getCategory(kotlin.String, com.adamratzman.spotify.utils.Market, kotlin.String)/categoryId) is not found or [locale](get-category.md#com.adamratzman.spotify.endpoints.public.BrowseAPI$getCategory(kotlin.String, com.adamratzman.spotify.utils.Market, kotlin.String)/locale) does not exist on Spotify