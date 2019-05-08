/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyRestActionPaging
import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encode
import com.adamratzman.spotify.models.BadRequestException
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.PlaylistTrack
import com.adamratzman.spotify.models.PlaylistURI
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.SpotifyImage
import com.adamratzman.spotify.models.UserURI
import com.adamratzman.spotify.models.serialization.toArray
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.models.serialization.toPagingObject
import com.adamratzman.spotify.utils.catch
import com.neovisionaries.i18n.CountryCode
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
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @return [PagingObject] of [SimplePlaylist]s **ONLY if** the user can be found. Otherwise, an empty paging object is returned.
     * This does not have the detail of full [Playlist] objects.
     *
     * @throws BadRequestException if the user is not found (404)
     *
     */
    fun getPlaylists(
        user: String,
        limit: Int? = null,
        offset: Int? = null
    ): SpotifyRestActionPaging<SimplePlaylist, PagingObject<SimplePlaylist>> {
        return toActionPaging(Supplier {
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
     * @param playlist the spotify id or uri for the playlist.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     *
     * @throws BadRequestException if the playlist is not found
     */
    fun getPlaylist(playlist: String, market: CountryCode? = null): SpotifyRestAction<Playlist?> {
        return toAction(Supplier {
            catch {
                get(
                        EndpointBuilder("/playlists/${PlaylistURI(playlist).id.encode()}")
                                .with("market", market?.name).toString()
                ).toObject<Playlist>(api)
            }
        })
    }

    /**
     * Get full details of the tracks of a playlist owned by a Spotify user.
     *
     * @param playlist the spotify id or uri for the playlist.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @throws BadRequestException if the playlist cannot be found
     */
    fun getPlaylistTracks(
        playlist: String,
        limit: Int? = null,
        offset: Int? = null,
        market: CountryCode? = null
    ): SpotifyRestActionPaging<PlaylistTrack, PagingObject<PlaylistTrack>> {
        return toActionPaging(Supplier {
            get(
                    EndpointBuilder("/playlists/${PlaylistURI(playlist).id.encode()}/tracks").with("limit", limit)
                            .with("offset", offset).with("market", market?.name).toString()
            )
                    .toPagingObject<PlaylistTrack>(null, this)
        })
    }

    /**
     * Get the current image associated with a specific playlist.
     * @param playlist the spotify id or uri for the playlist.
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
