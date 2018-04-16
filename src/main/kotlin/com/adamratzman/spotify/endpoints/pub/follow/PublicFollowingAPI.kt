package com.adamratzman.spotify.endpoints.pub.follow

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.SpotifyEndpoint
import com.adamratzman.spotify.utils.SpotifyRestAction
import com.adamratzman.spotify.utils.encode
import com.adamratzman.spotify.utils.toObject
import java.util.function.Supplier

/**
 * This endpoint allow you check the playlists that a Spotify user follows.
 */
class PublicFollowingAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Check to see if one or more Spotify users are following a specified playlist.
     *
     * @param playlistOwner Spotify ID of the creator of the playlist
     * @param playlistId Spotify playlist ID
     * @param userIds users to check
     *
     * @return List of Booleans representing whether the user follows the playlist. User IDs **not** found will return false
     *
     * @throws [BadRequestException] if the playlist is not found
     */
    fun doUsersFollowPlaylist(playlistOwner: String, playlistId: String, vararg userIds: String): SpotifyRestAction<List<Boolean>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/users/${playlistOwner.encode()}/playlists/${playlistId.encode()}/followers/contains?ids=${userIds.map { it.encode() }.joinToString(",")}").toObject<List<Boolean>>(api)
        })
    }
}