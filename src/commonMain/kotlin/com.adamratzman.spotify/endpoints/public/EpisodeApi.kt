/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encodeUrl
import com.adamratzman.spotify.models.Episode
import com.adamratzman.spotify.models.EpisodeList
import com.adamratzman.spotify.models.EpisodeUri
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.catch

class EpisodeApi(api: GenericSpotifyApi) : SpotifyEndpoint(api) {
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
     * Users can view the country that is associated with their account in the account settings.
     *
     * @return possibly-null [Episode].
     */
    fun getEpisode(id: String, market: Market? = null): SpotifyRestAction<Episode?> {
        return toAction {
            catch {
                get(
                        EndpointBuilder("/episodes/${EpisodeUri(id).id.encodeUrl()}").with("market", market?.name).toString()
                ).toObject(Episode.serializer(), api, json)
            }
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
     * @param ids The com.adamratzman.spotify id or uri for the episodes. Maximum **50**.
     * @param market If a country code is specified, only shows and episodes that are available in that market will be returned.
     * If a valid user access token is specified in the request header, the country associated with the user account will take priority over this parameter.
     * Note: If neither market or user country are provided, the content is considered unavailable for the client.
     * Users can view the country that is associated with their account in the account settings.
     *
     * @return List of possibly-null [Episode] objects.
     * @throws BadRequestException If any invalid show id is provided
     */
    fun getEpisodes(vararg ids: String, market: Market? = null): SpotifyRestAction<List<Episode?>> {
        checkBulkRequesting(50, ids.size)
        return toAction {
            bulkRequest(50, ids.toList()) { chunk ->
                get(
                        EndpointBuilder("/episodes").with("ids", chunk.joinToString(",") { EpisodeUri(it).id.encodeUrl() })
                                .with("market", market?.name).toString()
                ).toObject(EpisodeList.serializer(), api, json).episodes
            }.flatten()
        }
    }
}
