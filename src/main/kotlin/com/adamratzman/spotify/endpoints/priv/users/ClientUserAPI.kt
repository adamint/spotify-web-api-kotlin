package com.adamratzman.spotify.kotlin.endpoints.priv.users

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.obj.SpotifyEndpoint
import com.adamratzman.spotify.obj.SpotifyUserInformation
import com.adamratzman.spotify.obj.toObject

class ClientUserAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getUserProfile(): SpotifyUserInformation {
        return get("https://api.spotify.com/v1/me").toObject()
    }
}