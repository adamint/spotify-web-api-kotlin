package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.adamratzman.spotify.utils.*
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
            get(EndpointBuilder("/users/${playlistOwner.encode()}/playlists/${playlistId.encode()}/followers/contains")
                    .with("ids", userIds.joinToString(",") {it.encode()}).toString()).toObject<List<Boolean>>(api)
        })
    }
}