package com.adamratzman.spotify.endpoints.priv.personalization

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier

/**
 * Endpoints for retrieving information about the user’s listening habits.
 */
class PersonalizationAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
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
    fun getTopArtists(): SpotifyRestAction<PagingObject<Artist>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me/top/artists").toPagingObject<Artist>(api = api)
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
    fun getTopTracks(): SpotifyRestAction<PagingObject<Track>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me/top/tracks").toPagingObject<Track>(api = api)
        })
    }

}