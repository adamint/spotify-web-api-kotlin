package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encodeUrl
import com.adamratzman.spotify.models.SpotifyUri
import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.models.TrackUri
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.catch

/**
 * Endpoints for retrieving information about one or more shows and their episodes from the Spotify catalog.
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/shows/)**
 */
class ShowApi(api: SpotifyApi<*, *>) : SpotifyEndpoint(api) {
    /**
     * Get Spotify catalog information for a single show identified by its unique Spotify ID.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/tracks/get-track/)**
     *
     * @param id The Spotify ID for the show.
     *
     * @return possibly-null Show. This behavior is *the same* as in [getShows]
     */
    /*fun getShow(id: String): SpotifyRestAction<Show?> {
        return toAction {
            catch {
                SpotifyUri.
                get(
                        EndpointBuilder("/shows/${EpisodeUri(track).id.encodeUrl()}").with(
                                "market",
                                market?.name
                        ).toString()
                ).toObject(Track.serializer(), api, json)
            }
        }
    }*/
}