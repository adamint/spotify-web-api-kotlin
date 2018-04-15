package com.adamratzman.spotify.endpoints.priv.personalization

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier

/**
 * Endpoints for retrieving information about the user’s listening habits.
 */
class PersonalizationAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getTopArtists(): SpotifyRestAction<PagingObject<Artist>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me/top/artists").toPagingObject<Artist>(api = api)
        })
    }

    fun getTopTracks(): SpotifyRestAction<PagingObject<Track>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me/top/tracks").toPagingObject<Track>(api = api)
        })
    }

}