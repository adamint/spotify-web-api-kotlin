package com.adamratzman.spotify.endpoints.pub.follow

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.SpotifyEndpoint
import com.adamratzman.spotify.utils.SpotifyRestAction
import com.adamratzman.spotify.utils.toObject
import com.adamratzman.spotify.utils.encode
import java.util.function.Supplier

class PublicFollowingAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun doUsersFollowPlaylist(playlistOwner: String, playlistId: String, vararg userIds: String): SpotifyRestAction<List<Boolean>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/users/${playlistOwner.encode()}/playlists/${playlistId.encode()}/followers/contains?ids=${userIds.map { it.encode() }.joinToString(",")}").toObject<List<Boolean>>(api)
        })
    }
}