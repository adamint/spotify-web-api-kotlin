/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.pub

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.models.SpotifyPublicUser
import com.adamratzman.spotify.models.UserUri
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.catch
import com.adamratzman.spotify.utils.encodeUrl

/**
 * Endpoints for retrieving information about a user’s profile.
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/users-profile/)**
 */
public open class UserApi(api: GenericSpotifyApi) : SpotifyEndpoint(api) {
    /**
     * Get public profile information about a Spotify user.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/users-profile/get-users-profile/)**
     *
     * @param user The user’s Spotify user ID.
     *
     * @return All publicly-available information about the user
     */
    public suspend fun getProfile(user: String): SpotifyPublicUser? = catch(/* some incorrect user ids will return 500 */ catchInternalServerError = true) {
        get(endpointBuilder("/users/${UserUri(user).id.encodeUrl()}").toString())
            .toObject(SpotifyPublicUser.serializer(), api, json)
    }
}
