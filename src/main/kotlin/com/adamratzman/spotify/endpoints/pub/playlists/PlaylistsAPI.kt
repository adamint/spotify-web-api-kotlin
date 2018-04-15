package com.adamratzman.spotify.endpoints.pub.playlists

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier

/**
 * Endpoints for retrieving information about a user’s playlists
 */
class PlaylistsAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getPlaylists(userId: String, limit: Int = 20, offset: Int = 0): SpotifyRestAction<PagingObject<SimplePlaylist>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/users/${userId.encode()}/playlists?limit=$limit&offset=$offset").toPagingObject<SimplePlaylist>(api = api)
        })
    }

    fun getPlaylist(userId: String, playlistId: String, market: Market? = null): SpotifyRestAction<Playlist> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/users/${userId.encode()}/playlists/${playlistId.encode()}${if (market != null) "?market=${market.code}" else ""}").toObject<Playlist>(api)
        })
    }

    fun getPlaylistTracks(userId: String, playlistId: String, limit: Int = 20, offset: Int = 0, market: Market? = null): SpotifyRestAction<LinkedResult<PlaylistTrack>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/users/${userId.encode()}/playlists/${playlistId.encode()}/tracks?limit=$limit&offset=$offset${if (market != null) "&market=${market.code}" else ""}")
                    .toLinkedResult<PlaylistTrack>(api)
        })

    }

    fun getPlaylistCovers(userId: String, playlistId: String): SpotifyRestAction<List<SpotifyImage>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/users/${userId.encode()}/playlists/${playlistId.encode()}/images").toObject<List<SpotifyImage>>(api)
        })
    }
}