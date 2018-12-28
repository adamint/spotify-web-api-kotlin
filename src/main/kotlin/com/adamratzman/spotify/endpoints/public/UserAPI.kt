/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.adamratzman.spotify.utils.EndpointBuilder
import com.adamratzman.spotify.utils.SpotifyEndpoint
import com.adamratzman.spotify.utils.SpotifyPublicUser
import com.adamratzman.spotify.utils.UserURI
import com.adamratzman.spotify.utils.catch
import com.adamratzman.spotify.utils.encode
import com.adamratzman.spotify.utils.toObject
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
     *
     * @return null if the user cannot be found
     */
    fun getProfile(user: String): SpotifyRestAction<SpotifyPublicUser?> {
        return toAction(Supplier {
            catch {
                get(EndpointBuilder("/users/${UserURI(user).id.encode()}").toString()).toObject(api, SpotifyPublicUser::class.java)
            }
        })
    }
}
