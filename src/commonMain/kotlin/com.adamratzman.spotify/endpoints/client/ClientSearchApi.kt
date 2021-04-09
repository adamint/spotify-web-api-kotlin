/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.endpoints.pub.SearchApi
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.SimpleEpisode
import com.adamratzman.spotify.models.SimpleShow
import com.adamratzman.spotify.models.serialization.toNonNullablePagingObject
import com.adamratzman.spotify.utils.Market

/**
 * Get Spotify catalog information about artists, albums, tracks, playlists, episodes, or shows that match a keyword string.
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/search/search/)**
 */
public class ClientSearchApi(api: GenericSpotifyApi) : SearchApi(api) {

    /**
     * Get Spotify Catalog information about shows that match the keyword string. See [SearchApi.search] for more information
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/search/search/)**
     *
     * @param query Search query keywords and optional field filters and operators.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin#track-relinking)
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @see [SearchApi.search]
     *
     * @return [PagingObject] of non-full [SimpleShow] objects ordered by likelihood of correct match
     *
     * @throws BadRequestException if filters are illegal or query is malformed
     */
    public suspend fun searchShow(
        query: String,
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): PagingObject<SimpleShow> = get(build(query, market, limit, offset, SearchType.SHOW))
            .toNonNullablePagingObject(SimpleShow.serializer(), "shows", api, json)

    /**
     * Get Spotify Catalog information about shows that match the keyword string. See [SearchApi.search] for more information
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/search/search/)**
     *
     * @param query Search query keywords and optional field filters and operators.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin#track-relinking)
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @see [SearchApi.search]
     *
     * @return [PagingObject] of non-full [SimpleShow] objects ordered by likelihood of correct match
     *
     * @throws BadRequestException if filters are illegal or query is malformed
     */
    public fun searchShowRestAction(
        query: String,
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestAction<PagingObject<SimpleShow>> = SpotifyRestAction { searchShow(query, limit, offset, market) }

    /**
     * Get Spotify Catalog information about episodes that match the keyword string. See [SearchApi.search] for more information
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/search/search/)**
     *
     * @param query Search query keywords and optional field filters and operators.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin#track-relinking)
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @see [SearchApi.search]
     *
     * @return [PagingObject] of non-full [SimpleEpisode] objects ordered by likelihood of correct match
     *
     * @throws BadRequestException if filters are illegal or query is malformed
     */
    public suspend fun searchEpisode(
        query: String,
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): PagingObject<SimpleEpisode> = get(build(query, market, limit, offset, SearchType.EPISODE))
                .toNonNullablePagingObject(SimpleEpisode.serializer(), "episodes", api, json)

    /**
     * Get Spotify Catalog information about episodes that match the keyword string. See [SearchApi.search] for more information
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/search/search/)**
     *
     * @param query Search query keywords and optional field filters and operators.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin#track-relinking)
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @see [SearchApi.search]
     *
     * @return [PagingObject] of non-full [SimpleEpisode] objects ordered by likelihood of correct match
     *
     * @throws BadRequestException if filters are illegal or query is malformed
     */
    public fun searchEpisodeRestAction(
        query: String,
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestAction<PagingObject<SimpleEpisode>> = SpotifyRestAction { searchEpisode(query, limit, offset, market) }
}
