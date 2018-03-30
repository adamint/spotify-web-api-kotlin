package com.adamratzman.endpoints.priv.users

import com.adamratzman.main.SpotifyAPI
import com.adamratzman.main.toObject
import com.adamratzman.obj.SpotifyEndpoint
import com.adamratzman.obj.SpotifyUserInformation

class PrivateUserAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getUserProfile(): SpotifyUserInformation {
        return get("https://api.spotify.com/v1/me").toObject()
    }
}