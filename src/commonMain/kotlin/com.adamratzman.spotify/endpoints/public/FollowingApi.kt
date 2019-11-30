/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encodeUrl
import com.adamratzman.spotify.models.PlaylistUri
import com.adamratzman.spotify.models.UserUri
import com.adamratzman.spotify.models.serialization.toList
import kotlinx.serialization.list
import kotlinx.serialization.serializer

@Deprecated("Endpoint name has been updated for kotlin convention consistency", ReplaceWith("FollowingApi"))
typealias FollowingAPI = FollowingApi

/**
 * This endpoint allow you check the playlists that a Spotify user follows.
 */
open class FollowingApi(api: SpotifyApi<*, *>) : SpotifyEndpoint(api) {
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
        return toAction {
            val user = UserUri(playlistOwner)
            get(
                EndpointBuilder("/users/${user.id.encodeUrl()}/playlists/${PlaylistUri(playlist).id.encodeUrl()}/followers/contains")
                    .with("ids", users.joinToString(",") { UserUri(it).id.encodeUrl() }).toString()
            ).toList(Boolean.serializer().list, api, json)
        }
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
        return toAction {
            areFollowingPlaylist(
                playlistOwner,
                playlist,
                users = *arrayOf(user)
            ).complete()[0]
        }
    }
}
