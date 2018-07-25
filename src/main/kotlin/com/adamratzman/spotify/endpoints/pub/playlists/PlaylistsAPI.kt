package com.adamratzman.spotify.endpoints.pub.playlists

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier

/**
 * Endpoints for retrieving information about a user’s playlists
 */
class PlaylistsAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Get a list of the playlists owned or followed by a Spotify user.
     *
     * @param userId The user’s Spotify user ID.
     * @param limit The number of album objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first album to return. Default: 0 (i.e., the first album). Use with limit to get the next set of albums.
     *
     * @return [PagingObject] of [SimplePlaylist]s. This does not have the detail of full [Playlist] objects.
     *
     * @throws BadRequestException if the [userId] cannot be found
     */
    fun getPlaylists(userId: String, limit: Int = 20, offset: Int = 0): SpotifyRestAction<PagingObject<SimplePlaylist>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/users/${userId.encode()}/playlists?limit=$limit&offset=$offset").toPagingObject<SimplePlaylist>(api = api)
        })
    }

    /**
     * Get a playlist owned by a Spotify user.
     *
     * @param userId The user’s Spotify user ID.
     * @param playlistId The Spotify ID for the playlist.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     *
     * @throws BadRequestException if the playlist is not found
     */
    fun getPlaylist(userId: String, playlistId: String, market: Market? = null): SpotifyRestAction<Playlist?> {
        return toAction(Supplier {
            catch {get ("https://api.spotify.com/v1/users/${userId.encode()}/playlists/${playlistId.encode()}${if (market != null) "?market=${market.code}" else ""}").toObject<Playlist>(api)}
        })
    }

    /**
     * Get full details of the tracks of a playlist owned by a Spotify user.
     *
     * @param userId The user’s Spotify user ID.
     * @param playlistId The Spotify ID for the playlist.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     * @param limit The number of album objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first album to return. Default: 0 (i.e., the first album). Use with limit to get the next set of albums.
     *
     * @throws BadRequestException if the playlist cannot be found
     */
    fun getPlaylistTracks(userId: String, playlistId: String, limit: Int = 20, offset: Int = 0, market: Market? = null): SpotifyRestAction<LinkedResult<PlaylistTrack>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/users/${userId.encode()}/playlists/${playlistId.encode()}/tracks?limit=$limit&offset=$offset${if (market != null) "&market=${market.code}" else ""}")
                    .toLinkedResult<PlaylistTrack>(api)
        })

    }

    /**
     * Get the current image associated with a specific playlist.
     * @param userId The user’s Spotify user ID.
     * @param playlistId The Spotify ID for the playlist.
     *
     * @throws BadRequestException if the playlist cannot be found
     */
    fun getPlaylistCovers(userId: String, playlistId: String): SpotifyRestAction<List<SpotifyImage>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/users/${userId.encode()}/playlists/${playlistId.encode()}/images").toObject<List<SpotifyImage>>(api)
        })
    }
}