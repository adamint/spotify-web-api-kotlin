/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encode
import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.adamratzman.spotify.models.BadRequestException
import com.adamratzman.spotify.models.PlaylistURI
import com.adamratzman.spotify.models.UserURI
import com.adamratzman.spotify.models.serialization.toArray
import java.util.function.Supplier

/**
 * This endpoint allow you check the playlists that a Spotify user follows.
 */
open class FollowingAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Check to see if one or more Spotify users are following a specified playlist.
     *
     * @param playlistOwner id or uri of the creator of the playlist
     * @param playlist playlist id or uri
     * @param users user ids or uris to check
     *
     * @return List of Booleans representing whether the user follows the playlist. User IDs **not** found will return false
     *
     * @throws [BadRequestException] if the playlist is not found OR any user in the list does not exist
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
     * @param playlistOwner id or uri of the creator of the playlist
     * @param playlist playlist id or uri
     * @param user Spotify user id
     *
     * @return booleans representing whether the user follows the playlist. User IDs **not** found will return false
     *
     * @throws [BadRequestException] if the playlist is not found or if the user does not exist
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
