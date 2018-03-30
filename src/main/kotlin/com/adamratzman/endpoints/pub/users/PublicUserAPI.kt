package com.adamratzman.endpoints.pub.users

import com.adamratzman.main.SpotifyAPI
import com.adamratzman.main.toObject
import com.adamratzman.obj.SpotifyEndpoint
import com.adamratzman.obj.SpotifyPublicUser

class PublicUserAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getProfile(userId: String): SpotifyPublicUser? {
        return get("https://api.spotify.com/v1/users/$userId").toObject()
    }
}