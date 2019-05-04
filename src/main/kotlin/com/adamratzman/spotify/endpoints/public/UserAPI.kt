/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encode
import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.models.SpotifyPublicUser
import com.adamratzman.spotify.models.UserURI
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.catch
import java.util.function.Supplier

/**
 * Endpoints for retrieving information about a user’s profile.
 */
open class UserAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Get public profile information about a Spotify user.
     *
     * @param user The user’s Spotify user ID.
     *
     * @return publicly-available information about the user
     */
    fun getProfile(user: String): SpotifyRestAction<SpotifyPublicUser?> {
        return toAction(Supplier {
            catch {
                get(EndpointBuilder("/users/${UserURI(user).id.encode()}").toString())
                        .toObject<SpotifyPublicUser>(api)
            }
        })
    }
}
