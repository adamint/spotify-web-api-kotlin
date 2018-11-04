package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier

/**
 * Endpoints for retrieving information about a user’s profile.
 */
class PublicUserAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Get public profile information about a Spotify user.
     *
     * @param userId The user’s Spotify user ID.
     *
     * @return publicly-available information about the user
     *
     * @return null if the user cannot be found
     */
    fun getProfile(userId: String): SpotifyRestAction<SpotifyPublicUser?> {
        return toAction(Supplier {
            catch {
                get(EndpointBuilder("/users/${userId.encode()}").toString()).toObject<SpotifyPublicUser>(api)
            }
        })
    }
}