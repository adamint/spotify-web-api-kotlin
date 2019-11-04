/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.SpotifyRestActionPaging
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.models.serialization.toPagingObject

typealias ClientPersonalizationAPI = ClientPersonalizationApi

/**
 * Endpoints for retrieving information about the user’s listening habits.
 */
class ClientPersonalizationApi(api: SpotifyApi<*, *>) : SpotifyEndpoint(api) {
    /**
     * The time frame for which attribute affinities are computed.
     *
     * @param id the Spotify id of the time frame
     */
    enum class TimeRange(val id: String) {
        LONG_TERM("long_term"),
        MEDIUM_TERM("medium_term"),
        SHORT_TERM("short_term");

        override fun toString() = id
    }

    /**
     * Get the current user’s top artists based on calculated affinity.
     *
     * Affinity is a measure of the expected preference a user has for a particular track or artist.  It is based on user
     * behavior, including play history, but does not include actions made while in incognito mode. Light or infrequent
     * users of Spotify may not have sufficient play history to generate a full affinity data set. As a user’s behavior
     * is likely to shift over time, this preference data is available over three time spans. See time_range in the
     * query parameter table for more information. For each time range, the top 50 tracks and artists are available
     * for each user. In the future, it is likely that this restriction will be relaxed. This data is typically updated
     * once each day for each user.
     *
     * **Requires** the [SpotifyScope.USER_TOP_READ] scope
     *
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param timeRange The time range to which to compute this. The default is [TimeRange.MEDIUM_TERM]
     *
     * @return [PagingObject] of full [Artist] objects sorted by affinity
     */
    fun getTopArtists(
        limit: Int? = null,
        offset: Int? = null,
        timeRange: TimeRange? = null
    ): SpotifyRestActionPaging<Artist, PagingObject<Artist>> {
        return toActionPaging {
            get(
                EndpointBuilder("/me/top/artists").with("limit", limit).with("offset", offset)
                    .with("time_range", timeRange).toString()
            ).toPagingObject(Artist.serializer(), endpoint = this)
        }
    }

    /**
     * Get the current user’s top tracks based on calculated affinity.
     *
     * Affinity is a measure of the expected preference a user has for a particular track or artist.  It is based on user
     * behavior, including play history, but does not include actions made while in incognito mode. Light or infrequent
     * users of Spotify may not have sufficient play history to generate a full affinity data set. As a user’s behavior
     * is likely to shift over time, this preference data is available over three time spans. See time_range in the
     * query parameter table for more information. For each time range, the top 50 tracks and artists are available
     * for each user. In the future, it is likely that this restriction will be relaxed. This data is typically updated
     * once each day for each user.
     *
     * **Requires** the [SpotifyScope.USER_TOP_READ] scope
     *
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param timeRange The time range to which to compute this. The default is [TimeRange.MEDIUM_TERM]
     *
     * @return [PagingObject] of full [Track] objects sorted by affinity
     */
    fun getTopTracks(
        limit: Int? = null,
        offset: Int? = null,
        timeRange: TimeRange? = null
    ): SpotifyRestActionPaging<Track, PagingObject<Track>> {
        return toActionPaging {
            get(
                EndpointBuilder("/me/top/tracks").with("limit", limit).with("offset", offset)
                    .with("time_range", timeRange).toString()
            ).toPagingObject(Track.serializer(), endpoint = this)
        }
    }
}
