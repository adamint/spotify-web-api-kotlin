package com.adamratzman.spotify.endpoints.pub.users

import com.adamratzman.spotify.main.SpotifyAPI
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
     * @throws BadRequestException if the user cannot be found
     */
    fun getProfile(userId: String): SpotifyRestAction<SpotifyPublicUser> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/users/${userId.encode()}").toObject<SpotifyPublicUser>(api)
        })
    }
}