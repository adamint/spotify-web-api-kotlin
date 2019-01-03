/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.adamratzman.spotify.utils.BadRequestException
import com.adamratzman.spotify.utils.EndpointBuilder
import com.adamratzman.spotify.utils.LinkedResult
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.PagingObject
import com.adamratzman.spotify.utils.Playlist
import com.adamratzman.spotify.utils.PlaylistTrack
import com.adamratzman.spotify.utils.PlaylistURI
import com.adamratzman.spotify.utils.SimplePlaylist
import com.adamratzman.spotify.utils.SpotifyEndpoint
import com.adamratzman.spotify.utils.SpotifyImage
import com.adamratzman.spotify.utils.UserURI
import com.adamratzman.spotify.utils.catch
import com.adamratzman.spotify.utils.encode
import com.adamratzman.spotify.utils.toArray
import com.adamratzman.spotify.utils.toLinkedResult
import com.adamratzman.spotify.utils.toObject
import com.adamratzman.spotify.utils.toPagingObject
import java.util.function.Supplier

/**
 * Endpoints for retrieving information about a user’s playlists
 */
open class PlaylistsAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Get a list of the playlists owned or followed by a Spotify user. Lookups for non-existant users return empty [PagingObject]s
     * (blame Spotify)
     *
     * @param user The user’s Spotify user ID.
     * @param limit The number of album objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first album to return. Default: 0 (i.e., the first album). Use with limit to get the next set of albums.
     *
     * @return [PagingObject] of [SimplePlaylist]s **ONLY if** the user can be found. Otherwise, an empty paging object is returned.
     * This does not have the detail of full [Playlist] objects.
     *
     */
    fun getPlaylists(
        user: String,
        limit: Int? = null,
        offset: Int? = null
    ): SpotifyRestAction<PagingObject<SimplePlaylist>> {
        return toAction(Supplier {
            get(
                EndpointBuilder("/users/${UserURI(user).id.encode()}/playlists").with("limit", limit).with(
                    "offset", offset
                ).toString()
            ).toPagingObject<SimplePlaylist>(endpoint = this)
        })
    }

    /**
     * Get a playlist owned by a Spotify user.
     *
     * @param playlist The Spotify ID for the playlist.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     *
     * @throws BadRequestException if the playlist is not found
     */
    fun getPlaylist(playlist: String, market: Market? = null): SpotifyRestAction<Playlist?> {
        return toAction(Supplier {
            catch {
                get(
                    EndpointBuilder("/playlists/${PlaylistURI(playlist).id.encode()}")
                        .with("market", market?.code).toString()
                ).toObject<Playlist>(api)
            }
        })
    }

    /**
     * Get full details of the tracks of a playlist owned by a Spotify user.
     *
     * @param playlist The Spotify ID for the playlist.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     * @param limit The number of track objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first track to return. Default: 0 (i.e., the first album). Use with limit to get the next set of albums.
     *
     * @throws BadRequestException if the playlist cannot be found
     */
    fun getPlaylistTracks(
        playlist: String,
        limit: Int? = null,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestAction<LinkedResult<PlaylistTrack>> {
        return toAction(Supplier {
            get(
                EndpointBuilder("/playlists/${PlaylistURI(playlist).id.encode()}/tracks").with("limit", limit)
                    .with("offset", offset).with("market", market?.code).toString()
            )
                .toLinkedResult<PlaylistTrack>(api)
        })
    }

    /**
     * Get the current image associated with a specific playlist.
     * @param playlist The Spotify ID for the playlist.
     *
     * @throws BadRequestException if the playlist cannot be found
     */
    fun getPlaylistCovers(playlist: String): SpotifyRestAction<List<SpotifyImage>> {
        return toAction(Supplier {
            get(EndpointBuilder("/playlists/${PlaylistURI(playlist).id.encode()}/images").toString())
                .toArray<SpotifyImage>(api).toList()
        })
    }
}
