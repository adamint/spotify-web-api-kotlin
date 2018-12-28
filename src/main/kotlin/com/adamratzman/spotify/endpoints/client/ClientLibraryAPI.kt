package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier

/**
 * Endpoints for retrieving information about, and managing, tracks that the current user has saved in their “Your Music” library.
 */
class ClientLibraryAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    /**
     * Get a list of the songs saved in the current Spotify user’s ‘Your Music’ library.
     *
     * @return Paging Object of [SavedTrack] ordered by position in library
     */
    fun getSavedTracks(limit: Int? = null, offset: Int? = null, market: Market? = null): SpotifyRestAction<PagingObject<SavedTrack>> {
        return toAction(Supplier {
            get(EndpointBuilder("/me/tracks").with("limit", limit).with("offset", offset).with("market", market?.code)
                    .toString()).toPagingObject<SavedTrack>(endpoint = this)
        })
    }

    /**
     * Get a list of the albums saved in the current Spotify user’s ‘Your Music’ library.
     *
     * @return Paging Object of [SavedAlbum] ordered by position in library
     */
    fun getSavedAlbums(limit: Int? = null, offset: Int? = null, market: Market? = null): SpotifyRestAction<PagingObject<SavedAlbum>> {
        return toAction(Supplier {
            get(EndpointBuilder("/me/albums").with("limit", limit).with("offset", offset).with("market", market?.code)
                    .toString()).toPagingObject<SavedAlbum>(endpoint = this)
        })
    }

    /**
     * Check if the [LibraryType] with id [id] is already saved in the current Spotify user’s ‘Your Music’ library.
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
     * @throws BadRequestException if any of the provided ids is invalid
     */
    fun contains(type: LibraryType, vararg ids: String): SpotifyRestAction<List<Boolean>> {
        return toAction(Supplier {
            get(EndpointBuilder("/me/$type/contains").with("ids", ids.joinToString(",") { it.encode() })
                    .toString()).toObject<List<Boolean>>(api)
        })
    }

    /**
     * Save one of [LibraryType] to the current user’s ‘Your Music’ library.
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
     * @throws BadRequestException if any of the provided ids is invalid
     */
    fun add(type: LibraryType, vararg ids: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put(EndpointBuilder("/me/$type").with("ids", ids.joinToString(",") { it.encode() }).toString())
            Unit
        })
    }

    /**
     * Remove one of [LibraryType] (track or album) from the current user’s ‘Your Music’ library.
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
     * @throws BadRequestException if any of the provided ids is invalid
     */
    fun remove(type: LibraryType, vararg ids: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            delete(EndpointBuilder("/me/$type").with("ids", ids.joinToString(",") { it.encode() }).toString())
            Unit
        })
    }
}

enum class LibraryType(private val value: String) {
    TRACK("tracks"), ALBUM("albums");

    override fun toString() = value
}