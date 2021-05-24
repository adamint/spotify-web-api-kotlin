/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.models.*
import com.adamratzman.spotify.models.serialization.toList
import com.adamratzman.spotify.models.serialization.toNonNullablePagingObject
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.encodeUrl
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

/**
 * Endpoints for retrieving information about, and managing, tracks and albums that the current user has saved in their “Your Music” library.
 *
 * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/)**
 */
public class ClientLibraryApi(api: GenericSpotifyApi) : SpotifyEndpoint(api) {
    /**
     * Get a list of the songs saved in the current Spotify user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/get-users-saved-tracks/)**
     *
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     *
     * @return [PagingObject] of [SavedTrack] ordered by position in library
     */
    public suspend fun getSavedTracks(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): PagingObject<SavedTrack> {
        requireScopes(SpotifyScope.USER_LIBRARY_READ)

        return get(
            endpointBuilder("/me/tracks").with("limit", limit).with("offset", offset).with("market", market?.name)
                .toString()
        ).toNonNullablePagingObject(SavedTrack.serializer(), api = api, json = json)
    }

    /**
     * Get a list of the songs saved in the current Spotify user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/get-users-saved-tracks/)**
     *
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     *
     * @return [PagingObject] of [SavedTrack] ordered by position in library
     */
    public fun getSavedTracksRestAction(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestAction<PagingObject<SavedTrack>> = SpotifyRestAction { getSavedTracks(limit, offset, market) }

    /**
     * Get a list of the albums saved in the current Spotify user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/get-users-saved-albums/)**
     *
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     *
     * @return Paging Object of [SavedAlbum] ordered by position in library
     */
    public suspend fun getSavedAlbums(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): PagingObject<SavedAlbum> {
        requireScopes(SpotifyScope.USER_LIBRARY_READ)

        return get(
            endpointBuilder("/me/albums").with("limit", limit).with("offset", offset).with("market", market?.name)
                .toString()
        ).toNonNullablePagingObject(SavedAlbum.serializer(), api = api, json = json)
    }

    /**
     * Get a list of the albums saved in the current Spotify user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/get-users-saved-albums/)**
     *
     * @param limit The number of objects to return. Default: 50 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     *
     * @return Paging Object of [SavedAlbum] ordered by position in library
     */
    public fun getSavedAlbumsRestAction(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestAction<PagingObject<SavedAlbum>> = SpotifyRestAction { getSavedAlbums(limit, offset, market) }

    /**
     * Get a list of the episodes saved in the current Spotify user’s library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/#endpoint-get-users-saved-episodes)**
     *
     * @param limit The number of objects to return. Default: 20 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     *
     * @return Paging Object of [SavedEpisode] ordered by position in library
     */
    public suspend fun getSavedEpisodes(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): PagingObject<SavedEpisode> {
        requireScopes(SpotifyScope.USER_LIBRARY_READ)

        return get(
            endpointBuilder("/me/episodes").with("limit", limit).with("offset", offset).with("market", market?.name)
                .toString()
        ).toNonNullablePagingObject(SavedEpisode.serializer(), api = api, json = json)
    }

    /**
     * Get a list of the episodes saved in the current Spotify user’s library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/#endpoint-get-users-saved-episodes)**
     *
     * @param limit The number of objects to return. Default: 20 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     *
     * @return Paging Object of [SavedEpisode] ordered by position in library
     */
    public fun getSavedEpisodesRestAction(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestAction<PagingObject<SavedEpisode>> = SpotifyRestAction { getSavedEpisodes(limit, offset, market) }

    /**
     * Get a list of the shows saved in the current Spotify user’s library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/#endpoint-get-users-saved-shows)**
     *
     * @param limit The number of objects to return. Default: 20 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     *
     * @return Paging Object of [SavedShow] ordered by position in library
     */
    public suspend fun getSavedShows(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): PagingObject<SavedShow> {
        requireScopes(SpotifyScope.USER_LIBRARY_READ)

        return get(
            endpointBuilder("/me/shows").with("limit", limit).with("offset", offset).with("market", market?.name)
                .toString()
        ).toNonNullablePagingObject(SavedShow.serializer(), api = api, json = json)
    }

    /**
     * Get a list of the shows saved in the current Spotify user’s library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/#endpoint-get-users-saved-shows)**
     *
     * @param limit The number of objects to return. Default: 20 (or api limit). Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     *
     * @return Paging Object of [SavedEpisode] ordered by position in library
     */
    public fun getSavedShowsRestAction(
        limit: Int? = api.spotifyApiOptions.defaultLimit,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestAction<PagingObject<SavedShow>> = SpotifyRestAction { getSavedShows(limit, offset, market) }

    /**
     * Check if the [LibraryType] with id [id] is already saved in the current Spotify user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/)**
     *
     * @param type The type of object (album or track)
     * @param id The id or uri of the object
     *
     * @throws BadRequestException if [id] is not found
     */
    public suspend fun contains(type: LibraryType, id: String): Boolean = contains(type, ids = arrayOf(id))[0]

    /**
     * Check if the [LibraryType] with id [id] is already saved in the current Spotify user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/)**
     *
     * @param type The type of object (album or track)
     * @param id The id or uri of the object
     *
     * @throws BadRequestException if [id] is not found
     */
    public fun containsRestAction(type: LibraryType, id: String): SpotifyRestAction<Boolean> =
        SpotifyRestAction { contains(type, ids = arrayOf(id))[0] }

    /**
     * Check if one or more of [LibraryType] is already saved in the current Spotify user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/)**
     *
     * @param type The type of objects (album or track)
     * @param ids The ids or uris of the objects. Maximum **50** ids.
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    public suspend fun contains(type: LibraryType, vararg ids: String): List<Boolean> {
        requireScopes(SpotifyScope.USER_LIBRARY_READ)

        if (ids.size > 50 && !api.spotifyApiOptions.allowBulkRequests) throw BadRequestException(
            "Too many ids (${ids.size}) provided, only 50 allowed",
            IllegalArgumentException("Bulk requests are not turned on, and too many ids were provided")
        )
        return ids.toList().chunked(50).map { list ->
            get(
                endpointBuilder("/me/$type/contains").with("ids", list.joinToString(",") { type.id(it).encodeUrl() })
                    .toString()
            ).toList(ListSerializer(Boolean.serializer()), api, json)
        }.flatten()
    }

    /**
     * Check if one or more of [LibraryType] is already saved in the current Spotify user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_READ] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/)**
     *
     * @param type The type of objects (album or track)
     * @param ids The ids or uris of the objects. Maximum **50** ids.
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    public fun containsRestAction(type: LibraryType, vararg ids: String): SpotifyRestAction<List<Boolean>> {
        return SpotifyRestAction { contains(type, *ids) }
    }

    /**
     * Save one of [LibraryType] to the current user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/)**
     *
     * @param type The type of object (album or track)
     * @param id The id or uri of the object
     *
     * @throws BadRequestException if the id is invalid
     */
    public suspend fun add(type: LibraryType, id: String): Unit = add(type, ids = arrayOf(id))

    /**
     * Save one of [LibraryType] to the current user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/)**
     *
     * @param type The type of object (album or track)
     * @param id The id or uri of the object
     *
     * @throws BadRequestException if the id is invalid
     */
    public fun addRestAction(type: LibraryType, id: String): SpotifyRestAction<Unit> =
        SpotifyRestAction { add(type, id) }

    /**
     * Save one or more of [LibraryType] to the current user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/)**
     *
     * @param type The type of objects to check against (album or track)
     * @param ids The ids or uris of the objects. Maximum **50** ids.
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    public suspend fun add(type: LibraryType, vararg ids: String) {
        requireScopes(SpotifyScope.USER_LIBRARY_MODIFY)

        if (ids.size > 50 && !api.spotifyApiOptions.allowBulkRequests) throw BadRequestException(
            "Too many ids (${ids.size}) provided, only 50 allowed",
            IllegalArgumentException("Bulk requests are not turned on, and too many ids were provided")
        )
        ids.toList().chunked(50).forEach { list ->
            put(endpointBuilder("/me/$type").with("ids", list.joinToString(",") { type.id(it).encodeUrl() }).toString())
        }
    }

    /**
     * Save one or more of [LibraryType] to the current user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/)**
     *
     * @param type The type of objects to check against (album or track)
     * @param ids The ids or uris of the objects. Maximum **50** ids.
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    public fun addRestAction(type: LibraryType, vararg ids: String): SpotifyRestAction<Unit> =
        SpotifyRestAction { add(type, *ids) }

    /**
     * Remove one of [LibraryType] (track or album) from the current user’s ‘Your Music’ library.
     *
     * Changes to a user’s saved items may not be visible in other Spotify applications immediately.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/)**
     *
     * @param type The type of object to check against (album or track)
     * @param id The id or uri of the object
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    public suspend fun remove(type: LibraryType, id: String): Unit = remove(type, ids = arrayOf(id))

    /**
     * Remove one of [LibraryType] (track or album) from the current user’s ‘Your Music’ library.
     *
     * Changes to a user’s saved items may not be visible in other Spotify applications immediately.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/)**
     *
     * @param type The type of object to check against (album or track)
     * @param id The id or uri of the object
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    public fun removeRestAction(type: LibraryType, id: String): SpotifyRestAction<Unit> =
        SpotifyRestAction { remove(type, ids = arrayOf(id)) }

    /**
     * Remove one or more of the [LibraryType] (tracks or albums) from the current user’s ‘Your Music’ library.
     *
     * Changes to a user’s saved items may not be visible in other Spotify applications immediately.

     * **Requires** the [SpotifyScope.USER_LIBRARY_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/)**
     *
     * @param type The type of objects to check against (album or track)
     * @param ids The ids or uris of the objects. Maximum **50** ids.
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    public suspend fun remove(type: LibraryType, vararg ids: String) {
        requireScopes(SpotifyScope.USER_LIBRARY_MODIFY)

        if (ids.size > 50 && !api.spotifyApiOptions.allowBulkRequests) throw BadRequestException(
            "Too many ids (${ids.size}) provided, only 50 allowed",
            IllegalArgumentException("Bulk requests are not turned on, and too many ids were provided")
        )
        ids.toList().chunked(50).forEach { list ->
            delete(
                endpointBuilder("/me/$type").with(
                    "ids",
                    list.joinToString(",") { type.id(it).encodeUrl() }).toString()
            )
        }
    }

    /**
     * Remove one or more of the [LibraryType] (tracks or albums) from the current user’s ‘Your Music’ library.
     *
     * Changes to a user’s saved items may not be visible in other Spotify applications immediately.

     * **Requires** the [SpotifyScope.USER_LIBRARY_MODIFY] scope
     *
     * **[Api Reference](https://developer.spotify.com/documentation/web-api/reference/library/)**
     *
     * @param type The type of objects to check against (album or track)
     * @param ids The ids or uris of the objects. Maximum **50** ids.
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    public fun removeRestAction(type: LibraryType, vararg ids: String): SpotifyRestAction<Unit> = SpotifyRestAction {
        remove(type, *ids)
    }
}

/**
 * Type of object in a user's Spotify library
 *
 * @param value Spotify id for the type
 * @param id How to transform an id (or uri) input into its Spotify id
 */
public enum class LibraryType(private val value: String, internal val id: (String) -> String) {
    TRACK("tracks", { PlayableUri(it).id }),
    ALBUM("albums", { AlbumUri(it).id }),
    EPISODE("episodes", { EpisodeUri(it).id }),
    SHOW("shows", { ShowUri(it).id });

    override fun toString(): String = value
}
