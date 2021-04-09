/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.endpoints.pub.FollowingApi
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.ArtistUri
import com.adamratzman.spotify.models.CursorBasedPagingObject
import com.adamratzman.spotify.models.PlaylistUri
import com.adamratzman.spotify.models.UserUri
import com.adamratzman.spotify.models.serialization.toCursorBasedPagingObject
import com.adamratzman.spotify.models.serialization.toList
import com.adamratzman.spotify.utils.encodeUrl
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

/**
 * These endpoints allow you manage the artists, users and playlists that a Spotify user follows.
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/)**
 */
public class ClientFollowingApi(api: GenericSpotifyApi) : FollowingApi(api) {
    /**
     * Check to see if the current user is following another Spotify user.
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/check-current-user-follows/)**
     *
     * @param user The user id or uri to check.
     *
     * @throws BadRequestException if [user] is a non-existing id
     * @return Whether the current user is following [user]
     */
    public suspend fun isFollowingUser(user: String): Boolean = isFollowingUsers(user)[0]

    /**
     * Check to see if the current user is following another Spotify user.
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/check-current-user-follows/)**
     *
     * @param user The user id or uri to check.
     *
     * @throws BadRequestException if [user] is a non-existing id
     * @return Whether the current user is following [user]
     */
    public fun isFollowingUserRestAction(user: String): SpotifyRestAction<Boolean> =
        SpotifyRestAction { isFollowingUser(user) }

    /**
     * Check to see if the current Spotify user is following the specified playlist.
     *
     * Checking if the user is privately following a playlist is only possible for the current user when
     * that user has granted access to the [SpotifyScope.PLAYLIST_READ_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/check-user-following-playlist/)**
     *
     * @param playlistId playlist id or uri
     *
     * @return Boolean representing whether the user follows the playlist
     *
     * @throws [BadRequestException] if the playlist is not found
     * @return Whether the current user is following [playlistId]
     */
    public suspend fun isFollowingPlaylist(playlistId: String): Boolean =
        isFollowingPlaylist(
            playlistId,
            (api as SpotifyClientApi).getUserId()
        )

    /**
     * Check to see if the current Spotify user is following the specified playlist.
     *
     * Checking if the user is privately following a playlist is only possible for the current user when
     * that user has granted access to the [SpotifyScope.PLAYLIST_READ_PRIVATE] scope.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/check-user-following-playlist/)**
     *
     * @param playlistId playlist id or uri
     *
     * @return Boolean representing whether the user follows the playlist
     *
     * @throws [BadRequestException] if the playlist is not found
     * @return Whether the current user is following [playlistId]
     */
    public fun isFollowingPlaylistRestAction(playlistId: String) =
        SpotifyRestAction { isFollowingPlaylist(playlistId) }

    /**
     * Check to see if the current user is following one or more other Spotify users.
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/check-current-user-follows/)**
     *
     * @param users List of the user Spotify IDs to check. Max 50
     *
     * @throws BadRequestException if [users] contains a non-existing id
     * @return A list of booleans corresponding to [users] of whether the current user is following that user
     */
    public suspend fun isFollowingUsers(vararg users: String): List<Boolean> {
        checkBulkRequesting(50, users.size)
        return bulkRequest(50, users.toList()) { chunk ->
            get(
                endpointBuilder("/me/following/contains").with("type", "user")
                    .with("ids", chunk.joinToString(",") { UserUri(it).id.encodeUrl() }).toString()
            ).toList(ListSerializer(Boolean.serializer()), api, json)
        }.flatten()
    }

    /**
     * Check to see if the current user is following one or more other Spotify users.
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/check-current-user-follows/)**
     *
     * @param users List of the user Spotify IDs to check. Max 50
     *
     * @throws BadRequestException if [users] contains a non-existing id
     * @return A list of booleans corresponding to [users] of whether the current user is following that user
     */
    public fun isFollowingUsersRestAction(vararg users: String): SpotifyRestAction<List<Boolean>> =
        SpotifyRestAction { isFollowingUsers(*users) }

    /**
     * Check to see if the current user is following a Spotify artist.
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/check-current-user-follows/)**
     *
     * @param artist The artist id to check.
     *
     * @throws BadRequestException if [artist] is a non-existing id
     * @return Whether the current user is following [artist]
     */
    public suspend fun isFollowingArtist(artist: String): Boolean = isFollowingArtists(artist)[0]

    /**
     * Check to see if the current user is following a Spotify artist.
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/check-current-user-follows/)**
     *
     * @param artist The artist id to check.
     *
     * @throws BadRequestException if [artist] is a non-existing id
     * @return Whether the current user is following [artist]
     */
    public fun isFollowingArtistRestAction(artist: String): SpotifyRestAction<Boolean> =
        SpotifyRestAction { isFollowingArtist(artist) }

    /**
     * Check to see if the current user is following one or more artists.
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/check-current-user-follows/)**
     *
     * @param artists List of the artist ids or uris to check. Max 50
     *
     * @throws BadRequestException if [artists] contains a non-existing id
     * @return A list of booleans corresponding to [artists] of whether the current user is following that artist
     */
    public suspend fun isFollowingArtists(vararg artists: String): List<Boolean> {
        checkBulkRequesting(50, artists.size)
        return bulkRequest(50, artists.toList()) { chunk ->
            get(
                endpointBuilder("/me/following/contains").with("type", "artist")
                    .with("ids", chunk.joinToString(",") { ArtistUri(it).id.encodeUrl() }).toString()
            ).toList(ListSerializer(Boolean.serializer()), api, json)
        }.flatten()
    }

    /**
     * Check to see if the current user is following one or more artists.
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/check-current-user-follows/)**
     *
     * @param artists List of the artist ids or uris to check. Max 50
     *
     * @throws BadRequestException if [artists] contains a non-existing id
     * @return A list of booleans corresponding to [artists] of whether the current user is following that artist
     */
    public fun isFollowingArtistsRestAction(vararg artists: String): SpotifyRestAction<List<Boolean>> =
        SpotifyRestAction { isFollowingArtists(*artists) }

    /**
     * Get the current user’s followed artists.
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/get-followed/)**
     *
     * @param limit The maximum number of items to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param after The last artist ID retrieved from the previous request.
     *
     * @return [CursorBasedPagingObject] ([Information about them](https://github.com/adamint/com.adamratzman.spotify-web-api-kotlin/blob/master/README.md#the-benefits-of-linkedresults-pagingobjects-and-cursor-based-paging-objects)
     * with full [Artist] objects
     */
    public suspend fun getFollowedArtists(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        after: String? = null
    ): CursorBasedPagingObject<Artist> = get(
        endpointBuilder("/me/following").with("type", "artist").with("limit", limit).with(
            "after",
            after
        ).toString()
    ).toCursorBasedPagingObject(Artist::class, Artist.serializer(), "artists", api, json)

    /**
     * Get the current user’s followed artists.
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/get-followed/)**
     *
     * @param limit The maximum number of items to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param after The last artist ID retrieved from the previous request.
     *
     * @return [CursorBasedPagingObject] ([Information about them](https://github.com/adamint/com.adamratzman.spotify-web-api-kotlin/blob/master/README.md#the-benefits-of-linkedresults-pagingobjects-and-cursor-based-paging-objects)
     * with full [Artist] objects
     */
    public fun getFollowedArtistsRestAction(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        after: String? = null
    ): SpotifyRestAction<CursorBasedPagingObject<Artist>> = SpotifyRestAction { getFollowedArtists(limit, after) }

    /**
     * Add the current user as a follower of another user
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/follow-artists-users/)**
     *
     * @throws BadRequestException if an invalid id is provided
     */
    public suspend fun followUser(user: String): Unit = followUsers(user)

    /**
     * Add the current user as a follower of another user
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/follow-artists-users/)**
     *
     * @throws BadRequestException if an invalid id is provided
     */
    public fun followUserRestAction(user: String): SpotifyRestAction<Unit> = SpotifyRestAction { followUser(user) }

    /**
     * Add the current user as a follower of other users
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/follow-artists-users/)**
     *
     * @param users User ids or uris. Maximum **50**.
     *
     * @throws BadRequestException if an invalid id is provided
     */
    public suspend fun followUsers(vararg users: String) {
        checkBulkRequesting(50, users.size)
        bulkRequest(50, users.toList()) { chunk ->
            put(
                endpointBuilder("/me/following").with("type", "user")
                    .with("ids", chunk.joinToString(",") { UserUri(it).id.encodeUrl() }).toString()
            )
        }
    }

    /**
     * Add the current user as a follower of other users
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/follow-artists-users/)**
     *
     * @param users User ids or uris. Maximum **50**.
     *
     * @throws BadRequestException if an invalid id is provided
     */
    public fun followUsersRestAction(vararg users: String): SpotifyRestAction<Unit> =
        SpotifyRestAction { followUsers(*users) }

    /**
     * Add the current user as a follower of an artist
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/follow-artists-users/)**
     *
     * @throws BadRequestException if an invalid id is provided
     */
    public suspend fun followArtist(artistId: String): Unit = followArtists(artistId)

    /**
     * Add the current user as a follower of an artist
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/follow-artists-users/)**
     *
     * @throws BadRequestException if an invalid id is provided
     */
    public fun followArtistRestAction(artistId: String): SpotifyRestAction<Unit> = SpotifyRestAction { followArtist(artistId) }

    /**
     * Add the current user as a follower of other artists
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/follow-artists-users/)**
     *
     * @param artists User ids or uris. Maximum **50**.
     *
     * @throws BadRequestException if an invalid id is provided
     */
    public suspend fun followArtists(vararg artists: String) {
        checkBulkRequesting(50, artists.size)
        bulkRequest(50, artists.toList()) { chunk ->
            put(
                endpointBuilder("/me/following").with("type", "artist")
                    .with("ids", chunk.joinToString(",") { ArtistUri(it).id.encodeUrl() }).toString()
            )
        }
    }

    /**
     * Add the current user as a follower of other artists
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/follow-artists-users/)**
     *
     * @param artists User ids or uris. Maximum **50**.
     *
     * @throws BadRequestException if an invalid id is provided
     */
    public fun followArtistsRestAction(vararg artists: String): SpotifyRestAction<Unit> =
        SpotifyRestAction { followArtists(*artists) }

    /**
     * Add the current user as a follower of a playlist.
     *
     * Following a playlist publicly requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * following it privately requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * Note that the scopes you provide determine only whether the current user can themselves follow the playlist
     * publicly or privately (i.e. show others what they are following), not whether the playlist itself is
     * public or private.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/follow-playlist/)**
     *
     * @param playlist the id or uri of the playlist. Any playlist can be followed, regardless of its
     * public/private status, as long as you know its playlist ID.
     * @param followPublicly Defaults to true. If true the playlist will be included in user’s public playlists,
     * if false it will remain private. To be able to follow playlists privately, the user must have granted the playlist-modify-private scope.
     *
     * @throws BadRequestException if the playlist is not found
     */
    public suspend fun followPlaylist(playlist: String, followPublicly: Boolean = true): String = put(
        endpointBuilder("/playlists/${PlaylistUri(playlist).id}/followers").toString(),
        "{\"public\": $followPublicly}"
    )

    /**
     * Add the current user as a follower of a playlist.
     *
     * Following a playlist publicly requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * following it privately requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * Note that the scopes you provide determine only whether the current user can themselves follow the playlist
     * publicly or privately (i.e. show others what they are following), not whether the playlist itself is
     * public or private.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/follow-playlist/)**
     *
     * @param playlist the id or uri of the playlist. Any playlist can be followed, regardless of its
     * public/private status, as long as you know its playlist ID.
     * @param followPublicly Defaults to true. If true the playlist will be included in user’s public playlists,
     * if false it will remain private. To be able to follow playlists privately, the user must have granted the playlist-modify-private scope.
     *
     * @throws BadRequestException if the playlist is not found
     */
    public fun followPlaylistRestAction(playlist: String, followPublicly: Boolean): SpotifyRestAction<String> =
        SpotifyRestAction {
            followPlaylist(playlist, followPublicly)
        }

    /**
     * Remove the current user as a follower of another user
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/unfollow-artists-users/)**
     *
     * @param user The user to be unfollowed from
     *
     * @throws BadRequestException if [user] is not found
     */
    public suspend fun unfollowUser(user: String): Unit = unfollowUsers(user)

    /**
     * Remove the current user as a follower of another user
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/unfollow-artists-users/)**
     *
     * @param user The user to be unfollowed from
     *
     * @throws BadRequestException if [user] is not found
     */
    public fun unfollowUserRestAction(user: String): SpotifyRestAction<Unit> =
        SpotifyRestAction { unfollowUser(user) }

    /**
     * Remove the current user as a follower of other users
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/unfollow-artists-users/)**
     *
     * @param users The users to be unfollowed from. Maximum **50**.
     *
     * @throws BadRequestException if an invalid id is provided
     */
    public suspend fun unfollowUsers(vararg users: String) {
        checkBulkRequesting(50, users.size)
        bulkRequest(50, users.toList()) { list ->
            delete(
                endpointBuilder("/me/following").with("type", "user")
                    .with("ids", list.joinToString(",") { UserUri(it).id.encodeUrl() }).toString()
            )
        }
    }

    /**
     * Remove the current user as a follower of other users
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/unfollow-artists-users/)**
     *
     * @param users The users to be unfollowed from. Maximum **50**.
     *
     * @throws BadRequestException if an invalid id is provided
     */
    public fun unfollowUsersRestAction(vararg users: String): SpotifyRestAction<Unit> = SpotifyRestAction {
        unfollowUsers(*users)
    }

    /**
     * Remove the current user as a follower of an artist
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/unfollow-artists-users/)**
     *
     * @param artist The artist to be unfollowed from
     *
     * @throws BadRequestException if an invalid id is provided
     */
    public suspend fun unfollowArtist(artist: String): Unit = unfollowArtists(artist)

    /**
     * Remove the current user as a follower of an artist
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/unfollow-artists-users/)**
     *
     * @param artist The artist to be unfollowed from
     *
     * @throws BadRequestException if an invalid id is provided
     */
    public fun unfollowArtistRestAction(artist: String): SpotifyRestAction<Unit> = SpotifyRestAction {
        unfollowArtist(artist)
    }

    /**
     * Remove the current user as a follower of artists
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/unfollow-artists-users/)**
     *
     * @param artists The artists to be unfollowed from. Maximum **50**.
     *
     *
     * @throws BadRequestException if an invalid id is provided
     */
    public suspend fun unfollowArtists(vararg artists: String) {
        checkBulkRequesting(50, artists.size)
        bulkRequest(50, artists.toList()) { list ->
            delete(
                endpointBuilder("/me/following").with("type", "artist")
                    .with("ids", list.joinToString(",") { ArtistUri(it).id.encodeUrl() }).toString()
            )
        }
    }

    /**
     * Remove the current user as a follower of artists
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/unfollow-artists-users/)**
     *
     * @param artists The artists to be unfollowed from. Maximum **50**.
     *
     *
     * @throws BadRequestException if an invalid id is provided
     */
    public fun unfollowArtistsRestAction(vararg artists: String): SpotifyRestAction<Unit> = SpotifyRestAction {
        unfollowArtists(*artists)
    }

    /**
     * Remove the current user as a follower of a playlist.
     *
     * Unfollowing a publicly followed playlist for a user requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * unfollowing a privately followed playlist requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * Note that the scopes you provide relate only to whether the current user is following the playlist publicly or
     * privately (i.e. showing others what they are following), not whether the playlist itself is public or private.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/unfollow-playlist/)**
     *
     * @param playlist The id or uri of the playlist that is to be no longer followed.
     *
     * @throws BadRequestException if the playlist is not found
     */
    public suspend fun unfollowPlaylist(playlist: String): String =
        delete(endpointBuilder("/playlists/${PlaylistUri(playlist).id}/followers").toString())

    /**
     * Remove the current user as a follower of a playlist.
     *
     * Unfollowing a publicly followed playlist for a user requires authorization of the [SpotifyScope.PLAYLIST_MODIFY_PUBLIC] scope;
     * unfollowing a privately followed playlist requires the [SpotifyScope.PLAYLIST_MODIFY_PRIVATE] scope.
     *
     * Note that the scopes you provide relate only to whether the current user is following the playlist publicly or
     * privately (i.e. showing others what they are following), not whether the playlist itself is public or private.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/follow/unfollow-playlist/)**
     *
     * @param playlist The id or uri of the playlist that is to be no longer followed.
     *
     * @throws BadRequestException if the playlist is not found
     */
    public fun unfollowPlaylistRestAction(playlist: String): SpotifyRestAction<String> = SpotifyRestAction {
        unfollowPlaylist(playlist)
    }
}
