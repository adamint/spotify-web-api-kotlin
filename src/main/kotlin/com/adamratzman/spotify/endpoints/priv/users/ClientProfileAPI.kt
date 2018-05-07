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
class ClientProfileAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Get detailed profile information about the current user (including the current user’s username).
     *
     * The access token must have been issued on behalf of the current user.
     * Reading the user’s email address requires the user-read-email scope; reading country and product subscription level
     * requires the user-read-private scope. Reading the user’s birthdate requires the user-read-birthdate scope.
     *
     * @return Never-null [SpotifyUserInformation] object with possibly-null country, email, subscription and birthday fields
     */
    fun getUserProfile(): SpotifyRestAction<SpotifyUserInformation> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me").toObject<SpotifyUserInformation>(api)
        })
    }
}