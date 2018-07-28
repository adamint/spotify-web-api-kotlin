package com.adamratzman.spotify.endpoints.priv.follow

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier

/**
 * These endpoints allow you manage the artists, users and playlists that a Spotify user follows.
 */
class ClientFollowAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Check to see if the current user is following another Spotify users.
     *
     * @param userId Spotify ID to check.
     *
     * @throws BadRequestException if [userId] is a non-existing id
     */
    fun isFollowingUser(userId: String): SpotifyRestAction<Boolean> {
        return toAction(Supplier {
            isFollowingUsers(userId).complete()[0]
        })
    }

    /**
     * Check to see if the current user is following one or more other Spotify users.
     *
     * @param userIds List of the user Spotify IDs to check. Max 50
     *
     * @throws BadRequestException if [userIds] contains a non-existing id
     */
    fun isFollowingUsers(vararg userIds: String): SpotifyRestAction<List<Boolean>> {
        return toAction(Supplier {
            get(EndpointBuilder("/me/following/contains").with("type", "user")
                    .with("ids", userIds.joinToString(",") { it.encode() }).build()).toObject<List<Boolean>>(api)
        })
    }

    /**
     * Check to see if the current user is following a Spotify artist.
     *
     * @param artistId Spotify ID to check.
     *
     * @throws BadRequestException if [artistId] is a non-existing id
     */
    fun isFollowingArtist(artistId: String): SpotifyRestAction<Boolean> {
        return toAction(Supplier {
            isFollowingArtists(artistId).complete()[0]
        })
    }

    /**
     * Check to see if the current user is following one or more artists.
     *
     * @param artistIds List of the artist Spotify IDs to check. Max 50
     *
     * @throws BadRequestException if [artistIds] contains a non-existing id
     */
    fun isFollowingArtists(vararg artistIds: String): SpotifyRestAction<List<Boolean>> {
        return toAction(Supplier {
            get(EndpointBuilder("/me/following/contains").with("type", "artist")
                    .with("ids", artistIds.joinToString(",") { it.encode() }).build()).toObject<List<Boolean>>(api)
        })
    }

    /**
     * Get the current user’s followed artists.
     *
     * @return [CursorBasedPagingObject] ([Information about them](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#the-benefits-of-linkedresults-pagingobjects-and-cursor-based-paging-objects)
     * with full [Artist] objects
     */
    fun getFollowedArtists(limit: Int? = null, after: String? = null): SpotifyRestAction<CursorBasedPagingObject<Artist>> {
        return toAction(Supplier {
            get(EndpointBuilder("/me/following").with("type", "artist").with("limit", limit).with("after", after).build())
                    .toCursorBasedPagingObject<Artist>("artists", this)
        })
    }

    fun getFollowedUsers(): SpotifyRestAction<SpotifyPublicUser> = throw NotImplementedError("Though Spotify will implement this in the future, it is not currently supported.")

    /**
     * Add the current user as a follower of another user
     *
     * @throws BadRequestException if an invalid id is provided
     */
    fun followUser(userId: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            followUsers(userId).complete()
        })
    }

    /**
     * Add the current user as a follower of other users
     *
     * @throws BadRequestException if an invalid id is provided
     */
    fun followUsers(vararg userIds: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put(EndpointBuilder("/me/following").with("type", "user")
                    .with("ids", userIds.joinToString(",") { it.encode() }).build())
            Unit
        })
    }

    /**
     * Add the current user as a follower of an artist
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
     * @throws BadRequestException if an invalid id is provided
     */
    fun followArtists(vararg artistIds: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put(EndpointBuilder("/me/following").with("type", "artist")
                    .with("ids", artistIds.joinToString(",") { it.encode() }).build())
            Unit
        })
    }

    /**
     * Add the current user as a follower of a playlist.
     *
     * @param ownerId The Spotify user ID of the person who owns the playlist.
     * @param playlistId The Spotify ID of the playlist. Any playlist can be followed, regardless of its
     * public/private status, as long as you know its playlist ID.
     * @param followPublicly Defaults to true. If true the playlist will be included in user’s public playlists,
     * if false it will remain private. To be able to follow playlists privately, the user must have granted the playlist-modify-private scope.
     *
     * @throws BadRequestException if the playlist is not found
     */
    fun followPlaylist(ownerId: String, playlistId: String, followPublicly: Boolean = true): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put(EndpointBuilder("/users/$ownerId/playlists/$playlistId/followers").build(), "{\"public\": $followPublicly}")
            Unit
        })

    }

    /**
     * Remove the current user as a follower of another user
     *
     * @param userId The user to be unfollowed from
     *
     * @throws BadRequestException if [userId] is not found
     */
    fun unfollowUser(userId: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            unfollowUsers(userId).complete()
        })
    }

    /**
     * Remove the current user as a follower of other users
     *
     * @param userIds The users to be unfollowed from
     *
     * @throws BadRequestException if an invalid id is provided
     */
    fun unfollowUsers(vararg userIds: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            delete(EndpointBuilder("/me/following").with("type", "user")
                    .with("ids", userIds.joinToString(",") { it.encode() }).build())
            Unit
        })
    }

    /**
     * Remove the current user as a follower of an artist
     *
     * @param artistId The artist to be unfollowed from
     *
     * @throws BadRequestException if an invalid id is provided
     */
    fun unfollowArtist(artistId: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            unfollowArtists(artistId).complete()
        })
    }

    /**
     * Remove the current user as a follower of artists
     *
     * @param artistIds The artists to be unfollowed from
     *
     * @throws BadRequestException if an invalid id is provided
     */
    fun unfollowArtists(vararg artistIds: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            delete(EndpointBuilder("/me/following").with("type", "artist")
                    .with("ids", artistIds.joinToString(",") { it.encode() }).build())
            Unit
        })
    }

    /**
     * Remove the current user as a follower of a playlist.
     *
     * @param ownerId The Spotify user ID of the person who owns the playlist.
     * @param playlistId The Spotify ID of the playlist that is to be no longer followed.
     *
     * @throws BadRequestException if the playlist is not found
     */
    fun unfollowPlaylist(ownerId: String, playlistId: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            delete(EndpointBuilder("/users/$ownerId/playlists/$playlistId/followers").build())
            Unit
        })
    }
}