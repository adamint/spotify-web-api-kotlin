package com.adamratzman.endpoints.pub.follow

import com.adamratzman.main.SpotifyAPI
import com.adamratzman.main.toObject
import com.adamratzman.obj.SpotifyEndpoint

class PublicFollowingAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun doUsersFollowPlaylist(playlistOwner: String, playlistId: String, vararg userIds: String): List<Boolean> {
        return get("https://api.spotify.com/v1/users/$playlistOwner/playlists/$playlistId/followers/contains?ids=${userIds.joinToString(",")}").toObject()
    }
}