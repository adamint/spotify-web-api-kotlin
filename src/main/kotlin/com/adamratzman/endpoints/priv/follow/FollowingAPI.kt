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
    fun followUsers(vararg userIds: String) {
        put("https://api.spotify.com/v1/me/following?type=user&ids=${userIds.joinToString(",")}")
    }
    fun followArtists(vararg artistIds: String) {
        put("https://api.spotify.com/v1/me/following?type=artist&ids=${artistIds.joinToString(",")}")
    }
    fun followPlaylist(ownerId: String, playlistId:String, followPublicly: Boolean = true) {
        put("https://api.spotify.com/v1/users/$ownerId/playlists/$playlistId/followers", "{\"public\": $followPublicly}")
    }
    fun unfollowUsers(vararg userIds: String) {
        delete("https://api.spotify.com/v1/me/following?type=user&ids=${userIds.joinToString(",")}")
    }
    fun unfollowArtists(vararg artistIds: String) {
        delete("https://api.spotify.com/v1/me/following?type=artist&ids=${artistIds.joinToString(",")}")
    }
    fun unfollowPlaylist(ownerId: String, playlistId:String) {
        delete("https://api.spotify.com/v1/users/$ownerId/playlists/$playlistId/followers")
    }
}