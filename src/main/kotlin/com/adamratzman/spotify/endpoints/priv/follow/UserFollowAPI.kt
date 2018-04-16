package com.adamratzman.spotify.endpoints.priv.follow

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier

/**
 * These endpoints allow you manage the artists, users and playlists that a Spotify user follows.
 */
class UserFollowAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
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
            get("https://api.spotify.com/v1/me/following/contains?type=user&ids=${userIds.joinToString(",") { it.encode() }}").toObject<List<Boolean>>(api)
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
            get("https://api.spotify.com/v1/me/following/contains?type=artist&ids=${artistIds.joinToString(",") { it.encode() }}").toObject<List<Boolean>>(api)
        })
    }

    /**
     * Get the current user’s followed artists.
     *
     * @return [CursorBasedPagingObject] ([Information about them](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#the-benefits-of-linkedresults-pagingobjects-and-cursor-based-paging-objects)
     * with full [Artist] objects
     */
    fun getFollowedArtists(): SpotifyRestAction<CursorBasedPagingObject<Artist>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me/following?type=artist").toCursorBasedPagingObject<Artist>("artists", api)
        })
    }

    fun getFollowedUsers(): SpotifyRestAction<SpotifyPublicUser>
            = throw NotImplementedError("Though Spotify will implement this in the future, it is not currently supported.")

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
            put("https://api.spotify.com/v1/me/following?type=user&ids=${userIds.joinToString(",") { it.encode() }}")
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
            put("https://api.spotify.com/v1/me/following?type=artist&ids=${artistIds.joinToString(",")}")
            Unit
        })
    }


    fun followPlaylist(ownerId: String, playlistId: String, followPublicly: Boolean = true): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put("https://api.spotify.com/v1/users/$ownerId/playlists/$playlistId/followers", "{\"public\": $followPublicly}")
            Unit
        })

    }

    fun unfollowUsers(vararg userIds: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            delete("https://api.spotify.com/v1/me/following?type=user&ids=${userIds.joinToString(",") { it.encode() }}")
            Unit
        })
    }

    fun unfollowArtists(vararg artistIds: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            delete("https://api.spotify.com/v1/me/following?type=artist&ids=${artistIds.joinToString(",")}")
            Unit
        })
    }

    fun unfollowPlaylist(ownerId: String, playlistId: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            delete("https://api.spotify.com/v1/users/$ownerId/playlists/$playlistId/followers")
            Unit
        })
    }
}