/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.pub

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.Show
import com.adamratzman.spotify.models.ShowList
import com.adamratzman.spotify.models.ShowUri
import com.adamratzman.spotify.models.SimpleEpisode
import com.adamratzman.spotify.models.SimpleShow
import com.adamratzman.spotify.models.serialization.toNonNullablePagingObject
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.catch
import com.adamratzman.spotify.utils.encodeUrl

/**
 * Endpoints for retrieving information about one or more shows and their episodes from the Spotify catalog.
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/shows/)**
 */
public open class ShowApi(api: GenericSpotifyApi) : SpotifyEndpoint(api) {
    /**
     * Get Spotify catalog information for a single show identified by its unique Spotify ID.
     *
     * **Reading the user’s resume points on episode objects requires the [SpotifyScope.USER_READ_PLAYBACK_POSITION] scope**
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/tracks/get-track/)**
     *
     * @param id The Spotify ID for the show.
     * @param market If a country code is specified, only shows and episodes that are available in that market will be returned.
     * If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter.
     * Note: If neither market or user country are provided, the content is considered unavailable for the client.
     * Users can view the country that is associated with their account in the account settings. **Required for [SpotifyAppApi]**
     *
     * @return possibly-null Show. This behavior is *not the same* as in [getShows]
     */
    public suspend fun getShow(id: String, market: Market): Show? {
        return catch {
            get(
                endpointBuilder("/shows/${ShowUri(id).id.encodeUrl()}").with("market", market.name).toString()
            ).toObject(Show.serializer(), api, json)
        }
    }

    /**
     * Get Spotify catalog information for multiple shows based on their Spotify IDs.
     *
     * **Invalid show ids will result in a [BadRequestException]
     *
     * **Reading the user’s resume points on episode objects requires the [SpotifyScope.USER_READ_PLAYBACK_POSITION] scope**
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/shows/get-several-shows/)**
     *
     * @param ids The id or uri for the shows. Maximum **50**.
     * @param market If a country code is specified, only shows and episodes that are available in that market will be returned.
     * If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter.
     * Note: If neither market or user country are provided, the content is considered unavailable for the client.
     * Users can view the country that is associated with their account in the account settings. **Required for [SpotifyAppApi]**
     *
     * @return List of possibly-null [SimpleShow] objects.
     * @throws BadRequestException If any invalid show id is provided
     */
    public suspend fun getShows(vararg ids: String, market: Market): List<SimpleShow?> {
        checkBulkRequesting(50, ids.size)
        return bulkRequest(50, ids.toList()) { chunk ->
            get(
                endpointBuilder("/shows").with("ids", chunk.joinToString(",") { ShowUri(it).id.encodeUrl() })
                    .with("market", market.name).toString()
            ).toObject(ShowList.serializer(), api, json).shows
        }.flatten()
    }

    /**
     * Get Spotify catalog information about an show’s episodes.
     *
     * **Reading the user’s resume points on episode objects requires the [SpotifyScope.USER_READ_PLAYBACK_POSITION] scope**
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/shows/get-shows-episodes/)**
     *
     * @param id The Spotify ID for the show.
     * @param market If a country code is specified, only shows and episodes that are available in that market will be returned.
     * If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter.
     * Note: If neither market or user country are provided, the content is considered unavailable for the client.
     * Users can view the country that is associated with their account in the account settings. **Required for [SpotifyAppApi]**
     * @param limit The number of objects to return. Default: 20 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @throws BadRequestException if the playlist cannot be found
     */
    public suspend fun getShowEpisodes(
        id: String,
        limit: Int? = null,
        offset: Int? = null,
        market: Market
    ): PagingObject<SimpleEpisode> = get(
        endpointBuilder("/shows/${ShowUri(id).id.encodeUrl()}/episodes").with("limit", limit)
            .with("offset", offset).with("market", market.name).toString()
    ).toNonNullablePagingObject(SimpleEpisode.serializer(), null, api, json)
}
