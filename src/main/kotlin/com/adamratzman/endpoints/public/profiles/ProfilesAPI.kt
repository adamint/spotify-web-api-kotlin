package com.adamratzman.endpoints.public.profiles

import com.adamratzman.main.SpotifyAPI
import com.adamratzman.main.toObject
import com.adamratzman.obj.Endpoint
import com.adamratzman.obj.SpotifyPublicUser

class ProfilesAPI(api: SpotifyAPI) : Endpoint(api) {
    fun getProfile(userId: String): SpotifyPublicUser? {
        return get("https://api.spotify.com/v1/users/$userId").toObject()
    }
}