/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.endpoints.public.UserAPI
import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.models.SpotifyUserInformation
import com.adamratzman.spotify.models.serialization.toObject
import java.util.function.Supplier

/**
 * Endpoints for retrieving information about a user’s profile.
 */
class ClientUserAPI(api: SpotifyAPI) : UserAPI(api) {
    /**
     * Get detailed profile information about the current user (including the current user’s username).
     *
     * The access token must have been issued on behalf of the current user.
     * Reading the user’s email address requires the [SpotifyScope.USER_READ_EMAIL] scope;
     * reading country and product subscription level requires the [SpotifyScope.USER_READ_PRIVATE] scope.
     * Reading the user’s birthdate requires the [SpotifyScope.USER_READ_BIRTHDATE] scope.
     *
     * @return Never-null [SpotifyUserInformation] object with possibly-null country, email, subscription and birthday fields
     */
    fun getUserProfile(): SpotifyRestAction<SpotifyUserInformation> {
        return toAction(Supplier {
            get(EndpointBuilder("/me").toString()).toObject(api)
        })
    }
}