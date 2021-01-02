/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encodeUrl
import com.adamratzman.spotify.models.PlaylistUri
import com.adamratzman.spotify.models.UserUri
import com.adamratzman.spotify.models.serialization.toList
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

/**
 * This endpoint allow you check the playlists that a Spotify user follows.
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/)**
 */
public open class FollowingApi(api: GenericSpotifyApi) : SpotifyEndpoint(api) {
    /**
     * Check to see if one or more Spotify users are following a specified playlist.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/check-user-following-playlist/)**
     *
     * @param playlist playlist id or uri
     * @param users user ids or uris to check. Maximum **5**.
     *
     * @return List of Booleans representing whether the user follows the playlist. User IDs **not** found will return false
     *
     * @throws [BadRequestException] if the playlist is not found OR any user in the list does not exist
     */
    public suspend fun areFollowingPlaylist(
        playlist: String,
        vararg users: String
    ): List<Boolean> {
        checkBulkRequesting(5, users.size)

        return bulkRequest(5, users.toList()) { chunk ->
            get(
                endpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}/followers/contains")
                    .with("ids", chunk.joinToString(",") { UserUri(it).id.encodeUrl() }).toString()
            ).toList(ListSerializer(Boolean.serializer()), api, json)
        }.flatten()
    }

    /**
     * Check to see if a specific Spotify user is following the specified playlist.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/check-user-following-playlist/)**
     *
     * @param playlist playlist id or uri
     * @param user Spotify user id
     *
     * @return booleans representing whether the user follows the playlist. User IDs **not** found will return false
     *
     * @throws [BadRequestException] if the playlist is not found or if the user does not exist
     */
    public suspend fun isFollowingPlaylist(playlist: String, user: String): Boolean = areFollowingPlaylist(
        playlist,
        users = arrayOf(user)
    )[0]
}
