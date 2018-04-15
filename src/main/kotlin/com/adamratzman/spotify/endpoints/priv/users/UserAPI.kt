package com.adamratzman.spotify.endpoints.priv.users

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.SpotifyEndpoint
import com.adamratzman.spotify.utils.SpotifyRestAction
import com.adamratzman.spotify.utils.SpotifyUserInformation
import com.adamratzman.spotify.utils.toObject
import java.util.function.Supplier

/**
 * Endpoints for retrieving information about a user’s profile.
 */
class UserAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getUserProfile(): SpotifyRestAction<SpotifyUserInformation> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me").toObject<SpotifyUserInformation>(api)
        })
    }
}