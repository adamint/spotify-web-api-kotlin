/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.public

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.SpotifyScope
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
import com.adamratzman.spotify.models.serialization.toNonNullablePagingObject
import com.adamratzman.spotify.models.serialization.toObject
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.catch
import kotlinx.serialization.builtins.ListSerializer

/**
 * Endpoints for retrieving information about a user’s playlists
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/)**
 */
public open class PlaylistApi(api: GenericSpotifyApi) : SpotifyEndpoint(api) {
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
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/get-list-users-playlists/)**
     *
     * @param user The user’s Spotify user ID.
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @return [PagingObject] of [SimplePlaylist]s **ONLY if** the user can be found. Otherwise, an empty paging object is returned.
     * This does not have the detail of full [Playlist] objects.
     *
     * @throws BadRequestException if the user is not found (404)
     *
     */
    public suspend fun getUserPlaylists(
        user: String,
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null
    ): PagingObject<SimplePlaylist> = get(
        endpointBuilder("/users/${UserUri(user).id.encodeUrl()}/playlists").with("limit", limit).with(
            "offset", offset
        ).toString()
    ).toNonNullablePagingObject(SimplePlaylist.serializer(), api = api, json = json)

    @Deprecated("Renamed `getUserPlaylists`", ReplaceWith("getUserPlaylists"))
    public suspend fun getPlaylists(
        user: String,
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null
    ): PagingObject<SimplePlaylist> = getUserPlaylists(user, limit, offset)

    /**
     * Get a playlist owned by a Spotify user.
     *
     * **Note that** both Public and Private playlists belonging to any user are retrievable on provision of a valid access token.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/get-playlist/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin#track-relinking)
     *
     * @throws BadRequestException if the playlist is not found
     */
    public suspend fun getPlaylist(playlist: String, market: Market? = null): Playlist? = catch {
        get(
            endpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}")
                .with("market", market?.name).toString()
        ).toObject(Playlist.serializer(), api, json)
    }

    /**
     * Get full details of the tracks of a playlist owned by a Spotify user.
     *
     * **Note that** both Public and Private playlists belonging to any user are retrievable on provision of a valid access token.
     *
     * **Warning:** if the playlist contains podcasts, the tracks will be null if you are using [SpotifyAppApi].
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/get-playlists-tracks/)**
     *
     * @param playlist The id or uri for the playlist.
     * @param market Provide this parameter if you want to apply [Track Relinking](https://github.com/adamint/spotify-web-api-kotlin#track-relinking)
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     *
     * @throws BadRequestException if the playlist cannot be found
     */
    public suspend fun getPlaylistTracks(
        playlist: String,
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): PagingObject<PlaylistTrack> = get(
        endpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}/tracks").with("limit", limit)
            .with("offset", offset).with("market", market?.name).toString()
    )
        .toNonNullablePagingObject(PlaylistTrack.serializer(), null, api, json)

    /**
     * Get the current image(s) associated with a specific playlist.
     *
     * This access token must be issued on behalf of the user. Current playlist image for both Public and Private
     * playlists of any user are retrievable on provision of a valid access token.
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/playlists/get-playlist-cover/)**
     *
     * @param playlist The id or uri for the playlist.
     *
     * @throws BadRequestException if the playlist cannot be found
     */
    public suspend fun getPlaylistCovers(playlist: String): List<SpotifyImage> =
        get(endpointBuilder("/playlists/${PlaylistUri(playlist).id.encodeUrl()}/images").toString())
            .toList(ListSerializer(SpotifyImage.serializer()), api, json).toList()
}
