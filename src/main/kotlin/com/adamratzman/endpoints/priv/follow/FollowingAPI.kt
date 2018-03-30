package com.adamratzman.endpoints.priv.follow

import com.adamratzman.main.SpotifyAPI
import com.adamratzman.main.toCursorBasedPagingObject
import com.adamratzman.main.toObject
import com.adamratzman.obj.Artist
import com.adamratzman.obj.CursorBasedPagingObject
import com.adamratzman.obj.SpotifyEndpoint

class FollowingAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun followingUsers(vararg userIds: String): List<Boolean> {
        return get("https://api.spotify.com/v1/me/following/contains?type=user&ids=${userIds.joinToString(",")}").toObject()
    }

    fun followingArtists(vararg userIds: String): List<Boolean> {
        return get("https://api.spotify.com/v1/me/following/contains?type=artist&ids=${userIds.joinToString(",")}").toObject()
    }
    fun getFollowedArtists(): CursorBasedPagingObject<Artist> {
        return get("https://api.spotify.com/v1/me/following?type=artist").toCursorBasedPagingObject("artists")
    }
}