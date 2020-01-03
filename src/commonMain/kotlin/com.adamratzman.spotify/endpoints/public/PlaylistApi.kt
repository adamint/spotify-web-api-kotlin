/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyRestActionPaging
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encodeUrl
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.Playlist
import com.adamratzman.spotify.models.PlaylistTrack
import com.adamratzman.spotify.models.PlaylistUri
import com.adamratzman.spotify.models.SimplePlaylist
import com.adamratzman.spotify.models.SpotifyImage
import com.adamratzman.spotify.models.UserUri
import com.adamratzman.spotify.models.serialization.toList
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.models.serialization.toPagingObject
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.catch
import kotlinx.serialization.list

@Deprecated("Endpoint name has been updated for kotlin convention consistency", ReplaceWith("PlaylistApi"))
typealias PlaylistAPI = PlaylistApi

/**
 * Endpoints for retrieving information about a user’s playlists
 */
open class PlaylistApi(api: SpotifyApi<*, *>) : SpotifyEndpoint(api) {
    /**
     * Get a list of the playlists owned or followed by a Spotify user. Lookups for non-existant users return an empty
     * [PagingObject] (blame Spotify)
     *
     * **Note that** private playlists are only retrievable for the current user and require the [SpotifyScope.PLAYLIST_READ_PRIVATE] scope
     * to have been authorized by the user. Note that this scope alone will not return a collaborative playlist, even
     * though they are always private.
     * Collaborative playlists are only retrievable for the current user and require the [SpotifyScope.PLAYLIST_READ_COLLABORATIVE]
     * scope to have been authorized by the user.
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
        return toActionPaging {
            get(
                EndpointBuilder("/users/${UserUri(user).id.encodeUrl()}/playlists").with("limit", limit).with(
                    "offset", offset
                ).toString()
            ).toPagingObject(SimplePlaylist.serializer(), endpoint = this, json = json)
        }
    }

    /**
     * Get a playlist owned by a Spotify user.
     *
     * **Note that** both Public and Private playlists belonging to any user are retrievable on provision of a valid access token.
     *
     * @param playlist The spotify id or uri for the playlist.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin/blob/master/README.md#track-relinking)
     *
     * @throws BadRequestException if the playlist is not found
     */
    fun getPlaylist(playlist: String, market: Market? = null): SpotifyRestAction<Playlist?> {
        return toAction {
            catch {
                get(
                    EndpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}")
                        .with("market", market?.name).toString()
                ).toObject(Playlist.serializer(), api, json)
            }
        }
    }

    /**
     * Get full details of the tracks of a playlist owned by a Spotify user.
     *
     * **Note that** both Public and Private playlists belonging to any user are retrievable on provision of a valid access token.
     *
     * @param playlist The spotify id or uri for the playlist.
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
        market: Market? = null
    ): SpotifyRestActionPaging<PlaylistTrack, PagingObject<PlaylistTrack>> {
        return toActionPaging {
            get(
                EndpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}/tracks").with("limit", limit)
                    .with("offset", offset).with("market", market?.name).toString()
            )
                .toPagingObject(PlaylistTrack.serializer(), null, this, json)
        }
    }

    /**
     * Get the current image(s) associated with a specific playlist.
     *
     * This access token must be issued on behalf of the user. Current playlist image for both Public and Private
     * playlists of any user are retrievable on provision of a valid access token.
     *
     * @param playlist The spotify id or uri for the playlist.
     *
     * @throws BadRequestException if the playlist cannot be found
     */
    fun getPlaylistCovers(playlist: String): SpotifyRestAction<List<SpotifyImage>> {
        return toAction {
            get(EndpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}/images").toString())
                .toList(SpotifyImage.serializer().list, api, json).toList()
        }
    }
}
