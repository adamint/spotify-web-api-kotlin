package com.adamratzman.spotify.endpoints.priv.playlists

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import org.json.JSONObject
import java.util.function.Supplier

/**
 * Endpoints for retrieving information about a user’s playlists and for managing a user’s playlists.
 */
class UserPlaylistAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun createPlaylist(userId: String, name: String, description: String, public: Boolean = true): SpotifyRestAction<Playlist> {
        return toAction(Supplier {
            post("https://api.spotify.com/v1/users/${userId.encode()}/playlists",
                    "{" +
                            "\"name\": \"$name\"," +
                            "\"description\": \"$description\"," +
                            "\"public\": $public" +
                            "}").toObject<Playlist>(api)
        })
    }

    /**
     * [uris] <b>MUST BE</b> Spotify URIs, unlike nearly every other endpoint.
     */
    fun addTrackToPlaylist(userId: String, playlistId: String, vararg uris: String, position: Int? = null): SpotifyRestAction<Unit> {
        val json = JSONObject().put("uris", uris.map { "spotify:track:${it.encode()}" })
        if (position != null) json.put("position", position)
        return toAction(Supplier {
            post("https://api.spotify.com/v1/users/${userId.encode()}/playlists/${playlistId.encode()}/tracks", json.toString())
            Unit
        })
    }

    fun changePlaylistDescription(userId: String, playlistId: String, name: String? = null, public: Boolean? = null, collaborative: Boolean? = null,
                                  description: String? = null): SpotifyRestAction<Unit> {
        val json = JSONObject()
        if (name != null) json.put("name", name)
        if (public != null) json.put("public", public)
        if (collaborative != null) json.put("collaborative", collaborative)
        if (description != null) json.put("description", description)
        if (json.length() == 0) throw IllegalArgumentException("At least one option must not be null")
        return toAction(Supplier {
            put("https://api.spotify.com/v1/users/${userId.encode()}/playlists/${playlistId.encode()}", json.toString())
            Unit
        })
    }

    fun getClientPlaylists(limit: Int = 20, offset: Int = 0): SpotifyRestAction<PagingObject<SimplePlaylist>> {
        if (limit !in 1..50) throw IllegalArgumentException("Limit must be between 1 and 50. Provided $limit")
        if (offset !in 0..100000) throw IllegalArgumentException("Offset must be between 0 and 100,000. Provided $limit")
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me/playlists?limit=$limit&offset=$offset").toPagingObject<SimplePlaylist>(api = api)
        })
    }

    fun reorderTracks(userId: String, playlistId: String, reorderRangeStart: Int, reorderRangeLength: Int = 1, insertionPoint: Int, snapshotId: String? = null): SpotifyRestAction<Snapshot> {
        return toAction(Supplier {
            put("https://api.spotify.com/v1/users/${userId.encode()}/playlists/${playlistId.encode()}/tracks", "{" +
                    "\"range_start\": $reorderRangeStart," +
                    "\"range_length\": $reorderRangeLength," +
                    "\"insert_before\": $insertionPoint" +
                    if (snapshotId != null) ",\"snapshot_id\": \"$snapshotId\"" else "" +
                            "}").toObject<Snapshot>(api)
        })
    }

    fun replaceTracks(userId: String, playlistId: String, vararg trackIds: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put("https://api.spotify.com/v1/users/${userId.encode()}/playlists/${playlistId.encode()}/tracks?uris=${trackIds.map { it.encode() }.joinToString(",") { "spotify:track:$it" }}")
            Unit
        })
    }

    data class Snapshot(val snapshot_id: String)
}