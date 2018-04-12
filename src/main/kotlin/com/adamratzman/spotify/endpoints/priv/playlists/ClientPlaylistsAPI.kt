package com.adamratzman.spotify.kotlin.endpoints.priv.playlists

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.obj.*
import org.json.JSONObject

class ClientPlaylistsAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun createPlaylist(userId: String, name: String, description: String, public: Boolean = true): Playlist {
        return post("https://api.spotify.com/v1/users/$userId/playlists",
                "{" +
                        "\"name\": \"$name\"," +
                        "\"description\": \"$description\"," +
                        "\"public\": $public" +
                        "}").toObject()
    }

    /**
     * [uris] <b>MUST BE</b> Spotify URIs, unlike nearly every other endpoint.
     */
    fun addTrackToPlaylist(userId: String, playlistId: String, vararg uris: String, position: Int? = null) {
        val json = JSONObject().put("uris", uris.map { "spotify:track:$it" })
        if (position != null) json.put("position", position)
        post("https://api.spotify.com/v1/users/$userId/playlists/$playlistId/tracks", json.toString())
    }

    fun changePlaylistDescription(userId: String, playlistId: String, name: String? = null, public: Boolean? = null, collaborative: Boolean? = null,
                                  description: String? = null) {
        val json = JSONObject()
        if (name != null) json.put("name", name)
        if (public != null) json.put("public", public)
        if (collaborative != null) json.put("collaborative", collaborative)
        if (description != null) json.put("description", description)
        if (json.length() == 0) throw IllegalArgumentException("At least one option must not be null")
        put("https://api.spotify.com/v1/users/$userId/playlists/$playlistId", json.toString())
    }

    fun getClientPlaylists(limit: Int = 20, offset: Int = 0): PagingObject<SimplePlaylist> {
        if (limit !in 1..50) throw IllegalArgumentException("Limit must be between 1 and 50. Provided $limit")
        if (offset !in 0..100000) throw IllegalArgumentException("Offset must be between 0 and 100,000. Provided $limit")
        return get("https://api.spotify.com/v1/me/playlists?limit=$limit&offset=$offset").toPagingObject()
    }

    fun reorderTracks(userId: String, playlistId: String, reorderRangeStart: Int, reorderRangeLength: Int = 1, insertionPoint: Int, snapshotId: String? = null): Snapshot {
        return put("https://api.spotify.com/v1/users/$userId/playlists/$playlistId/tracks", "{" +
                "\"range_start\": $reorderRangeStart," +
                "\"range_length\": $reorderRangeLength," +
                "\"insert_before\": $insertionPoint" +
                if (snapshotId != null) ",\"snapshot_id\": \"$snapshotId\"" else "" +
                        "}").toObject()
    }

    fun replaceTracks(userId: String, playlistId: String, vararg trackIds: String) {
        put("https://api.spotify.com/v1/users/$userId/playlists/$playlistId/tracks?uris=${trackIds.joinToString(",") { "spotify:track:$it" }}")
    }

    data class Snapshot(val snapshot_id: String)
}