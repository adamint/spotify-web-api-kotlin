/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encodeUrl
import com.adamratzman.spotify.models.SpotifyPublicUser
import com.adamratzman.spotify.models.UserUri
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.catch

typealias UserAPI = UserApi

/**
 * Endpoints for retrieving information about a user’s profile.
 */
open class UserApi(api: SpotifyApi<*, *>) : SpotifyEndpoint(api) {
    /**
     * Get public profile information about a Spotify user.
     *
     * @param user The user’s Spotify user ID.
     *
     * @return All publicly-available information about the user
     */
    fun getProfile(user: String): SpotifyRestAction<SpotifyPublicUser?> {
        return toAction {
            catch {
                get(EndpointBuilder("/users/${UserUri(user).id.encodeUrl()}").toString())
                    .toObject(SpotifyPublicUser.serializer(), api)
            }
        }
    }
}
