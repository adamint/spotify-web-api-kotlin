/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.models.serialization.toPagingObject

/**
 * Endpoints for retrieving information about the user’s listening habits.
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/personalization/)**
 */
public class ClientPersonalizationApi(api: GenericSpotifyApi) : SpotifyEndpoint(api) {
    /**
     * The time frame for which attribute affinities are computed.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/personalization/get-users-top-artists-and-tracks/)**
     *
     * @param id the Spotify id of the time frame
     */
    public enum class TimeRange(public val id: String) {
        /**
         * Calculated from several years of data and including all new data as it becomes available
         */
        LONG_TERM("long_term"),

        /**
         * Approximately last 6 months
         */
        MEDIUM_TERM("medium_term"),

        /**
         * Approximately last 4 weeks
         */
        SHORT_TERM("short_term");

        override fun toString(): String = id
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
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/personalization/get-users-top-artists-and-tracks/)**
     *
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param timeRange The time range to which to compute this. The default is [TimeRange.MEDIUM_TERM]
     *
     * @return [PagingObject] of full [Artist] objects sorted by affinity
     */
    public suspend fun getTopArtists(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        timeRange: TimeRange? = null
    ): PagingObject<Artist> = get(
        endpointBuilder("/me/top/artists").with("limit", limit).with("offset", offset)
            .with("time_range", timeRange).toString()
    ).toPagingObject(Artist.serializer(), endpoint = this, json = json)

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
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/personalization/get-users-top-artists-and-tracks/)**
     *
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param timeRange The time range to which to compute this. The default is [TimeRange.MEDIUM_TERM]
     *
     * @return [PagingObject] of full [Track] objects sorted by affinity
     */
    public suspend fun getTopTracks(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        timeRange: TimeRange? = null
    ): PagingObject<Track> = get(
        endpointBuilder("/me/top/tracks").with("limit", limit).with("offset", offset)
            .with("time_range", timeRange).toString()
    ).toPagingObject(Track.serializer(), endpoint = this, json = json)
}
