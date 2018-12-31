/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestPagingAction
import com.adamratzman.spotify.utils.Artist
import com.adamratzman.spotify.utils.EndpointBuilder
import com.adamratzman.spotify.utils.PagingObject
import com.adamratzman.spotify.utils.SpotifyEndpoint
import com.adamratzman.spotify.utils.Track
import com.adamratzman.spotify.utils.toPagingObject
import java.util.function.Supplier

/**
 * Endpoints for retrieving information about the user’s listening habits.
 */
class ClientPersonalizationAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    enum class TimeRange(val id: String) {
        LONG_TERM("long_term"), MEDIUM_TERM("medium_term"), SHORT_TERM("short_term");

        override fun toString() = id
    }

    /**
     * Get the current user’s top artists based on calculated affinity.
     * Affinity is a measure of the expected preference a user has for a particular track or artist.  It is based on user
     * behavior, including play history, but does not include actions made while in incognito mode. Light or infrequent
     * users of Spotify may not have sufficient play history to generate a full affinity data set. As a user’s behavior
     * is likely to shift over time, this preference data is available over three time spans. See time_range in the
     * query parameter table for more information. For each time range, the top 50 tracks and artists are available
     * for each user. In the future, it is likely that this restriction will be relaxed. This data is typically updated
     * once each day for each user.
     *
     * @return [PagingObject] of full [Artist] objects sorted by affinity
     */
    fun getTopArtists(limit: Int? = null, offset: Int? = null, timeRange: TimeRange? = null): SpotifyRestPagingAction<Artist, PagingObject<Artist>> {
        return toPagingObjectAction(Supplier {
            get(EndpointBuilder("/me/top/artists").with("limit", limit).with("offset", offset)
                    .with("time_range", timeRange).toString())
                .toPagingObject(endpoint = this, serializer = Artist.serializer())
        })
    }

    /**
     * Get the current user’s top tracks based on calculated affinity.
     * Affinity is a measure of the expected preference a user has for a particular track or artist.  It is based on user
     * behavior, including play history, but does not include actions made while in incognito mode. Light or infrequent
     * users of Spotify may not have sufficient play history to generate a full affinity data set. As a user’s behavior
     * is likely to shift over time, this preference data is available over three time spans. See time_range in the
     * query parameter table for more information. For each time range, the top 50 tracks and artists are available
     * for each user. In the future, it is likely that this restriction will be relaxed. This data is typically updated
     * once each day for each user.
     *
     * @return [PagingObject] of full [Track] objects sorted by affinity
     */
    fun getTopTracks(limit: Int? = null, offset: Int? = null, timeRange: TimeRange? = null): SpotifyRestPagingAction<Track, PagingObject<Track>> {
        return toPagingObjectAction(Supplier {
            get(EndpointBuilder("/me/top/tracks").with("limit", limit).with("offset", offset)
                    .with("time_range", timeRange).toString())
                .toPagingObject(endpoint = this, serializer = Track.serializer())
        })
    }
}