/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.adamratzman.spotify.utils.BadRequestException
import com.adamratzman.spotify.utils.EndpointBuilder
import com.adamratzman.spotify.utils.PlaylistURI
import com.adamratzman.spotify.utils.SpotifyEndpoint
import com.adamratzman.spotify.utils.UserURI
import com.adamratzman.spotify.utils.encode
import com.adamratzman.spotify.utils.toArray
import java.util.function.Supplier

/**
 * This endpoint allow you check the playlists that a Spotify user follows.
 */
open class FollowingAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Check to see if one or more Spotify users are following a specified playlist.
     *
     * @param playlistOwner Spotify ID of the creator of the playlist
     * @param playlist Spotify playlist ID
     * @param users users to check
     *
     * @return List of Booleans representing whether the user follows the playlist. User IDs **not** found will return false
     *
     * @throws [BadRequestException] if the playlist is not found
     */
    fun areFollowingPlaylist(
        playlistOwner: String,
        playlist: String,
        vararg users: String
    ): SpotifyRestAction<List<Boolean>> {
        return toAction(Supplier {
            val user = UserURI(playlistOwner)
            get(
                EndpointBuilder("/users/${user.id.encode()}/playlists/${PlaylistURI(playlist).id.encode()}/followers/contains")
                    .with("ids", users.joinToString(",") { UserURI(it).id.encode() }).toString()
            ).toArray<Boolean>(api)
        })
    }

    /**
     * Check to see if a specific Spotify user is following the specified playlist.
     *
     * @param playlistOwner Spotify ID of the creator of the playlist
     * @param playlist Spotify playlist ID
     * @param user Spotify user id
     *
     * @return booleans representing whether the user follows the playlist. User IDs **not** found will return false
     *
     * @throws [BadRequestException] if the playlist is not found
     */
    fun isFollowingPlaylist(playlistOwner: String, playlist: String, user: String): SpotifyRestAction<Boolean> {
        return toAction(Supplier {
            areFollowingPlaylist(
                playlistOwner,
                playlist,
                users = *arrayOf(user)
            ).complete()[0]
        })
    }
}
