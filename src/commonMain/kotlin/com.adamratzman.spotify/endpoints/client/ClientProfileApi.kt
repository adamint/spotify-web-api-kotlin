/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.endpoints.public.UserApi
import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.models.SpotifyUserInformation
import com.adamratzman.spotify.models.serialization.toObject

@Deprecated("Endpoint name has been updated for kotlin convention consistency", ReplaceWith("ClientProfileApi"))
typealias ClientUserAPI = ClientProfileApi

/**
 * Endpoints for retrieving information about a user’s profile.
 *
 * **[Api Reference](https://developer.com.adamratzman.spotify.com/documentation/web-api/reference/users-profile/)**
 */
class ClientProfileApi(api: GenericSpotifyApi) : UserApi(api) {
    /**
     * Get detailed profile information about the current user (including the current user’s username).
     *
     * The access token must have been issued on behalf of the current user.
     * Reading the user’s email address requires the [SpotifyScope.USER_READ_EMAIL] scope;
     * reading country and product subscription level requires the [SpotifyScope.USER_READ_PRIVATE] scope.
     * Reading the user’s birthdate requires the [SpotifyScope.USER_READ_BIRTHDATE] scope.
     *
     * **[Api Reference](https://developer.com.adamratzman.spotify.com/documentation/web-api/reference/users-profile/get-current-users-profile/)**
     *
     * @return Never-null [SpotifyUserInformation] object with possibly-null country, email, subscription and birthday fields
     */
    fun getClientProfile(): SpotifyRestAction<SpotifyUserInformation> {
        return toAction {
            get(EndpointBuilder("/me").toString()).toObject(SpotifyUserInformation.serializer(), api, json)
        }
    }

    @Deprecated("Renamed to use `client` instead of `user`", ReplaceWith("getClientProfile"))
    fun getUserProfile() = getClientProfile()
}
