package com.adamratzman.endpoints.public.playlists

import com.adamratzman.main.SpotifyAPI
import com.adamratzman.main.toObject
import com.adamratzman.obj.*

class PlaylistsAPI(api: SpotifyAPI) : Endpoint(api) {
    fun getPlaylists(userId: String, limit: Int = 20, offset: Int = 0): SimplePlaylistPagingObject {
        return get("https://api.spotify.com/v1/users/$userId/playlists?limit=$limit&offset=$offset").toObject()
    }

    fun getPlaylist(userId: String, playlistId: String, market: String? = null): Playlist? {
        return get("https://api.spotify.com/v1/users/$userId/playlists/$playlistId${if (market != null) "?market=$market" else ""}").toObject()
    }

    fun getPlaylistTracks(userId: String, playlistId: String, limit: Int = 20, offset: Int = 0, market: String? = null): PlaylistTrackLinkedResult {
        return get("https://api.spotify.com/v1/users/$userId/playlists/$playlistId/tracks?limit=$limit&offset=$offset${if (market != null) "&market=$market" else ""}").toObject()
    }
}