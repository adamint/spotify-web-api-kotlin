package com.adamratzman.spotify.endpoints.pub.users

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.obj.SpotifyEndpoint
import com.adamratzman.spotify.obj.SpotifyPublicUser
import com.adamratzman.spotify.obj.toObject

class PublicUserAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getProfile(userId: String): SpotifyPublicUser? {
        return get("https://api.spotify.com/v1/users/$userId").toObject(api)
    }
}