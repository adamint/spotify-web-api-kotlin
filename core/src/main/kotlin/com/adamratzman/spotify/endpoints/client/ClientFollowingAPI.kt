/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.SpotifyClientAPI
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyRestActionPaging
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.endpoints.public.FollowingAPI
import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.encode
import com.adamratzman.spotify.models.Artist
import com.adamratzman.spotify.models.ArtistURI
import com.adamratzman.spotify.models.CursorBasedPagingObject
import com.adamratzman.spotify.models.PlaylistURI
import com.adamratzman.spotify.models.UserURI
import com.adamratzman.spotify.models.serialization.toCursorBasedPagingObject
import com.adamratzman.spotify.models.serialization.toList
import java.util.function.Supplier

/**
 * These endpoints allow you manage the artists, users and playlists that a Spotify user follows.
 */
class ClientFollowingAPI(api: SpotifyAPI) : FollowingAPI(api) {
    /**
     * Check to see if the current user is following another Spotify user.
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_READ] scope
     *
     * @param user The user id or uri to check.
     *
     * @throws BadRequestException if [user] is a non-existing id
     * @return Whether the current user is following [user]
     */
    fun isFollowingUser(user: String): SpotifyRestAction<Boolean> {
        return toAction(Supplier {
            isFollowingUsers(user).complete()[0]
        })
    }

    /**
     * Check to see if the current Spotify user is following the specified playlist.
     *
     * Checking if the user is privately following a playlist is only possible for the current user when
     * that user has granted access to the [SpotifyScope.PLAYLIST_READ_PRIVATE] scope.
     *
     * @param playlistOwner id or uri of the creator of the playlist
     * @param playlistId playlist id or uri
     *
     * @return Boolean representing whether the user follows the playlist
     *
     * @throws [BadRequestException] if the playlist is not found
     * @return Whether the current user is following [playlistId]
     */
    fun isFollowingPlaylist(playlistOwner: String, playlistId: String): SpotifyRestAction<Boolean> {
        return toAction(Supplier {
            isFollowingPlaylist(
                    playlistOwner,
                    playlistId,
                    (api as SpotifyClientAPI).userId
            ).complete()
        })
    }

    /**
     * Check to see if the current user is following one or more other Spotify users.
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_READ] scope
     *
     * @param users List of the user Spotify IDs to check. Max 50
     *
     * @throws BadRequestException if [users] contains a non-existing id
     * @return A list of booleans corresponding to [users] of whether the current user is following that user
     */
    fun isFollowingUsers(vararg users: String): SpotifyRestAction<List<Boolean>> {
        return toAction(Supplier {
            get(
                    EndpointBuilder("/me/following/contains").with("type", "user")
                            .with("ids", users.joinToString(",") { UserURI(it).id.encode() }).toString()
            ).toList<Boolean>(api)
        })
    }

    /**
     * Check to see if the current user is following a Spotify artist.
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_READ] scope
     *
     * @param artist The artist id to check.
     *
     * @throws BadRequestException if [artist] is a non-existing id
     * @return Whether the current user is following [artist]
     */
    fun isFollowingArtist(artist: String): SpotifyRestAction<Boolean> {
        return toAction(Supplier {
            isFollowingArtists(artist).complete()[0]
        })
    }

    /**
     * Check to see if the current user is following one or more artists.
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_READ] scope
     *
     * @param artists List of the artist ids or uris to check. Max 50
     *
     * @throws BadRequestException if [artists] contains a non-existing id
     * @return A list of booleans corresponding to [artists] of whether the current user is following that artist
     */
    fun isFollowingArtists(vararg artists: String): SpotifyRestAction<List<Boolean>> {
        return toAction(Supplier {
            get(
                    EndpointBuilder("/me/following/contains").with("type", "artist")
                            .with("ids", artists.joinToString(",") { ArtistURI(it).id.encode() }).toString()
            ).toList<Boolean>(api)
        })
    }

    /**
     * Get the current user’s followed artists.
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_READ] scope
     *
     * @param limit The maximum number of items to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param after The last artist ID retrieved from the previous request.
     *
     * @return [CursorBasedPagingObject] ([Information about them](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#the-benefits-of-linkedresults-pagingobjects-and-cursor-based-paging-objects)
     * with full [Artist] objects
     */
    fun getFollowedArtists(
        limit: Int? = null,
        after: String? = null
    ): SpotifyRestActionPaging<Artist, CursorBasedPagingObject<Artist>> {
        return toActionPaging(Supplier {
            get(
                    EndpointBuilder("/me/following").with("type", "artist").with("limit", limit).with(
                            "after",
                            after
                    ).toString()
            ).toCursorBasedPagingObject<Artist>("artists", this)
        })
    }

    /**
     * Add the current user as a follower of another user
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * @throws BadRequestException if an invalid id is provided
     */
    fun followUser(user: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            followUsers(user).complete()
        })
    }

    /**
     * Add the current user as a follower of other users
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * @throws BadRequestException if an invalid id is provided
     */
    fun followUsers(vararg users: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put(
                    EndpointBuilder("/me/following").with("type", "user")
                            .with("ids", users.joinToString(",") { UserURI(it).id.encode() }).toString()
            )
            Unit
        })
    }

    /**
     * Add the current user as a follower of an artist
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * @throws BadRequestException if an invalid id is provided
     */
    fun followArtist(artistId: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            followArtists(artistId).complete()
        })
    }

    /**
     * Add the current user as a follower of other artists
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * @throws BadRequestException if an invalid id is provided
     */
    fun followArtists(vararg artists: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put(
                    EndpointBuilder("/me/following").with("type", "artist")
                            .with("ids", artists.joinToString(",") { ArtistURI(it).id.encode() }).toString()
            )
            Unit
        })
    }

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
     * @param playlist the spotify id or uri of the playlist. Any playlist can be followed, regardless of its
     * public/private status, as long as you know its playlist ID.
     * @param followPublicly Defaults to true. If true the playlist will be included in user’s public playlists,
     * if false it will remain private. To be able to follow playlists privately, the user must have granted the playlist-modify-private scope.
     *
     * @throws BadRequestException if the playlist is not found
     */
    fun followPlaylist(playlist: String, followPublicly: Boolean = true): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put(
                    EndpointBuilder("/playlists/${PlaylistURI(playlist).id}/followers").toString(),
                    "{\"public\": $followPublicly}"
            )
            Unit
        })
    }

    /**
     * Remove the current user as a follower of another user
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * @param user The user to be unfollowed from
     *
     * @throws BadRequestException if [user] is not found
     */
    fun unfollowUser(user: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            unfollowUsers(user).complete()
        })
    }

    /**
     * Remove the current user as a follower of other users
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * @param users The users to be unfollowed from
     *
     * @throws BadRequestException if an invalid id is provided
     */
    fun unfollowUsers(vararg users: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            delete(
                    EndpointBuilder("/me/following").with("type", "user")
                            .with("ids", users.joinToString(",") { UserURI(it).id.encode() }).toString()
            )
            Unit
        })
    }

    /**
     * Remove the current user as a follower of an artist
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * @param artist The artist to be unfollowed from
     *
     * @throws BadRequestException if an invalid id is provided
     */
    fun unfollowArtist(artist: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            unfollowArtists(artist).complete()
        })
    }

    /**
     * Remove the current user as a follower of artists
     *
     * **Requires** the [SpotifyScope.USER_FOLLOW_MODIFY] scope
     *
     * @param artists The artists to be unfollowed from
     *
     * @throws BadRequestException if an invalid id is provided
     */
    fun unfollowArtists(vararg artists: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            delete(
                    EndpointBuilder("/me/following").with("type", "artist")
                            .with("ids", artists.joinToString(",") { ArtistURI(it).id.encode() }).toString()
            )
            Unit
        })
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
     * @param playlist The spotify id or uri of the playlist that is to be no longer followed.
     *
     * @throws BadRequestException if the playlist is not found
     */
    fun unfollowPlaylist(playlist: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            delete(EndpointBuilder("/playlists/${PlaylistURI(playlist).id}/followers").toString())
            Unit
        })
    }
}
