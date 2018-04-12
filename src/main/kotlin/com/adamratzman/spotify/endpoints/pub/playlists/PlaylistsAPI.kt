package com.adamratzman.spotify.kotlin.endpoints.pub.playlists

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.obj.*

class PlaylistsAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getPlaylists(userId: String, limit: Int = 20, offset: Int = 0): PagingObject<SimplePlaylist> {
        return get("https://api.spotify.com/v1/users/$userId/playlists?limit=$limit&offset=$offset").toPagingObject()
    }

    fun getPlaylist(userId: String, playlistId: String, market: String? = null): Playlist? {
        return get("https://api.spotify.com/v1/users/$userId/playlists/$playlistId${if (market != null) "?market=$market" else ""}").toObject()
    }

    fun getPlaylistTracks(userId: String, playlistId: String, limit: Int = 20, offset: Int = 0, market: String? = null): LinkedResult<PlaylistTrack> {
        return get("https://api.spotify.com/v1/users/$userId/playlists/$playlistId/tracks?limit=$limit&offset=$offset${if (market != null) "&market=$market" else ""}")
                .toLinkedResult()
    }

    fun getPlaylistCovers(userId: String, playlistId: String): List<SpotifyImage> {
        return get("https://api.spotify.com/v1/users/$userId/playlists/$playlistId/images").toObject()
    }
}