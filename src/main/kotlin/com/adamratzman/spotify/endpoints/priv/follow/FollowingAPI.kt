package com.adamratzman.spotify.endpoints.priv.follow

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier

class FollowingAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun followingUsers(vararg userIds: String): SpotifyRestAction<List<Boolean>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me/following/contains?type=user&ids=${userIds.joinToString(",") { it.encode() }} { it.encode() }}").toObject<List<Boolean>>(api)
        })
    }

    fun followingArtists(vararg userIds: String): SpotifyRestAction<List<Boolean>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me/following/contains?type=artist&ids=${userIds.joinToString(",") { it.encode() }}").toObject<List<Boolean>>(api)
        })
    }

    fun getFollowedArtists(): SpotifyRestAction<CursorBasedPagingObject<Artist>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me/following?type=artist").toCursorBasedPagingObject<Artist>("artists", api)
        })
    }

    fun followUsers(vararg userIds: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put("https://api.spotify.com/v1/me/following?type=user&ids=${userIds.joinToString(",") { it.encode() }}")
            Unit
        })
    }

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