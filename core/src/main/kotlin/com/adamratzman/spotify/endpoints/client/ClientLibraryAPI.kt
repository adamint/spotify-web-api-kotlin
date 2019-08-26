/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyRestActionPaging
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.http.EndpointBuilder
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.http.encode
import com.adamratzman.spotify.models.AlbumURI
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.SavedAlbum
import com.adamratzman.spotify.models.SavedTrack
import com.adamratzman.spotify.models.TrackURI
import com.adamratzman.spotify.models.serialization.toList
import com.adamratzman.spotify.models.serialization.toPagingObject
import com.neovisionaries.i18n.CountryCode
import java.util.function.Supplier

/**
 * Endpoints for retrieving information about, and managing, tracks that the current user has saved in their “Your Music” library.
 */
class ClientLibraryAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Get a list of the songs saved in the current Spotify user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_READ] scope
     *
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     *
     * @return [PagingObject] of [SavedTrack] ordered by position in library
     */
    fun getSavedTracks(
        limit: Int? = null,
        offset: Int? = null,
        market: CountryCode? = null
    ): SpotifyRestActionPaging<SavedTrack, PagingObject<SavedTrack>> {
        return toActionPaging(Supplier {
            get(
                    EndpointBuilder("/me/tracks").with("limit", limit).with("offset", offset).with("market", market?.name)
                            .toString()
            ).toPagingObject<SavedTrack>(endpoint = this)
        })
    }

    /**
     * Get a list of the albums saved in the current Spotify user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_READ] scope
     *
     * @param limit The number of objects to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0. Use with limit to get the next set of items
     * @param market Provide this parameter if you want the list of returned items to be relevant to a particular country.
     * If omitted, the returned items will be relevant to all countries.
     *
     * @return Paging Object of [SavedAlbum] ordered by position in library
     */
    fun getSavedAlbums(
        limit: Int? = null,
        offset: Int? = null,
        market: CountryCode? = null
    ): SpotifyRestActionPaging<SavedAlbum, PagingObject<SavedAlbum>> {
        return toActionPaging(Supplier {
            get(
                    EndpointBuilder("/me/albums").with("limit", limit).with("offset", offset).with("market", market?.name)
                            .toString()
            ).toPagingObject<SavedAlbum>(endpoint = this)
        })
    }

    /**
     * Check if the [LibraryType] with id [id] is already saved in the current Spotify user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_READ] scope
     *
     * @param type The type of object (album or track)
     * @param id The spotify id or uri of the object
     *
     * @throws BadRequestException if [id] is not found
     */
    fun contains(type: LibraryType, id: String): SpotifyRestAction<Boolean> {
        return toAction(Supplier {
            contains(type, ids = *arrayOf(id)).complete()[0]
        })
    }

    /**
     * Check if one or more of [LibraryType] is already saved in the current Spotify user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_READ] scope
     *
     * @param type The type of objects (album or track)
     * @param ids The spotify ids or uris of the objects
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    fun contains(type: LibraryType, vararg ids: String): SpotifyRestAction<List<Boolean>> {
        return toAction(Supplier {
            get(
                    EndpointBuilder("/me/$type/contains").with("ids", ids.joinToString(",") { type.id(it).encode() })
                            .toString()
            ).toList<Boolean>(api)
        })
    }

    /**
     * Save one of [LibraryType] to the current user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_MODIFY] scope
     *
     * @param type The type of object (album or track)
     * @param id The spotify id or uri of the object
     *
     * @throws BadRequestException if the id is invalid
     */
    fun add(type: LibraryType, id: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            add(type, ids = *arrayOf(id)).complete()
        })
    }

    /**
     * Save one or more of [LibraryType] to the current user’s ‘Your Music’ library.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_MODIFY] scope
     *
     * @param type The type of objects to check against (album or track)
     * @param ids The spotify ids or uris of the objects
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    fun add(type: LibraryType, vararg ids: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put(EndpointBuilder("/me/$type").with("ids", ids.joinToString(",") { type.id(it).encode() }).toString())
            Unit
        })
    }

    /**
     * Remove one of [LibraryType] (track or album) from the current user’s ‘Your Music’ library.
     *
     * Changes to a user’s saved items may not be visible in other Spotify applications immediately.
     *
     * **Requires** the [SpotifyScope.USER_LIBRARY_MODIFY] scope
     *
     * @param type The type of object to check against (album or track)
     * @param id The spotify id or uri of the object
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    fun remove(type: LibraryType, id: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            remove(type, ids = *arrayOf(id)).complete()
        })
    }

    /**
     * Remove one or more of the [LibraryType] (tracks or albums) from the current user’s ‘Your Music’ library.
     *
     * Changes to a user’s saved items may not be visible in other Spotify applications immediately.

     * **Requires** the [SpotifyScope.USER_LIBRARY_MODIFY] scope
     *
     * @param type The type of objects to check against (album or track)
     * @param ids The spotify ids or uris of the objects
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    fun remove(type: LibraryType, vararg ids: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            delete(EndpointBuilder("/me/$type").with("ids", ids.joinToString(",") { type.id(it).encode() }).toString())
            Unit
        })
    }
}

/**
 * Type of object in a user's Spotify library
 *
 * @param value Spotify id for the type
 * @param id How to transform an id (or uri) input into its Spotify id
 */
enum class LibraryType(private val value: String, internal val id: (String) -> String) {
    TRACK("tracks", { TrackURI(it).id }),
    ALBUM("albums", { AlbumURI(it).id });

    override fun toString() = value
}
