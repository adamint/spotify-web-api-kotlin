package com.adamratzman.spotify.endpoints.pub.users

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier

class PublicUserAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getProfile(userId: String): SpotifyRestAction<SpotifyPublicUser> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/users/${userId.encode()}").toObject<SpotifyPublicUser>(api)
        })
    }
}