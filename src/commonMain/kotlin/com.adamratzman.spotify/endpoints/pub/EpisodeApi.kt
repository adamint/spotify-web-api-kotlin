/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.pub

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.models.Episode
import com.adamratzman.spotify.models.EpisodeList
import com.adamratzman.spotify.models.EpisodeUri
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.catch
import com.adamratzman.spotify.utils.encodeUrl

/**
 * Endpoints for retrieving information about one or more episodes from the Spotify catalog.
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/episodes/)**
 */
public open class EpisodeApi(api: GenericSpotifyApi) : SpotifyEndpoint(api) {
    /**
     * Get Spotify catalog information for a single episode identified by its unique Spotify ID.
     *
     * **Reading the user’s resume points on episode objects requires the [SpotifyScope.USER_READ_PLAYBACK_POSITION] scope**
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/episodes/get-an-episode/)**
     *
     * @param id The Spotify ID for the episode.
     * @param market If a country code is specified, only shows and episodes that are available in that market will be returned.
     * If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter.
     * Note: If neither market or user country are provided, the content is considered unavailable for the client.
     * Users can view the country that is associated with their account in the account settings. Required for [SpotifyAppApi], but **you may use [Market.FROM_TOKEN] to get the user market**
     *
     * @return possibly-null [Episode].
     */
    public suspend fun getEpisode(id: String, market: Market): Episode? {
        return catch {
            get(
                endpointBuilder("/episodes/${EpisodeUri(id).id.encodeUrl()}").with("market", market.name).toString()
            ).toObject(Episode.serializer(), api, json)
        }
    }

    /**
     * Get Spotify catalog information for multiple episodes based on their Spotify IDs.
     *
     * **Invalid episode ids will result in a [BadRequestException]
     *
     * **Reading the user’s resume points on episode objects requires the [SpotifyScope.USER_READ_PLAYBACK_POSITION] scope**
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/episodes/get-several-episodes/)**
     *
     * @param ids The id or uri for the episodes. Maximum **50**.
     * @param market If a country code is specified, only shows and episodes that are available in that market will be returned.
     * If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter.
     * Note: If neither market or user country are provided, the content is considered unavailable for the client.
     * Users can view the country that is associated with their account in the account settings. Required for [SpotifyAppApi], but **you may use [Market.FROM_TOKEN] to get the user market**
     *
     * @return List of possibly-null [Episode] objects.
     * @throws BadRequestException If any invalid show id is provided
     */
    public suspend fun getEpisodes(vararg ids: String, market: Market): List<Episode?> {
        checkBulkRequesting(50, ids.size)

        return bulkStatelessRequest(50, ids.toList()) { chunk ->
            get(
                endpointBuilder("/episodes").with("ids", chunk.joinToString(",") { EpisodeUri(it).id.encodeUrl() })
                    .with("market", market.name).toString()
            ).toObject(EpisodeList.serializer(), api, json).episodes
        }.flatten()
    }
}
