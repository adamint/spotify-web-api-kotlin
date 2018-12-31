/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.endpoints.client

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.adamratzman.spotify.main.SpotifyRestPagingAction
import com.adamratzman.spotify.utils.AlbumURI
import com.adamratzman.spotify.utils.EndpointBuilder
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.PagingObject
import com.adamratzman.spotify.utils.SavedAlbum
import com.adamratzman.spotify.utils.SavedTrack
import com.adamratzman.spotify.utils.SpotifyEndpoint
import com.adamratzman.spotify.utils.TrackURI
import com.adamratzman.spotify.utils.encode
import com.adamratzman.spotify.utils.toObject
import com.adamratzman.spotify.utils.toPagingObject
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.internal.BooleanSerializer
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
    fun getSavedTracks(
        limit: Int? = null,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestPagingAction<SavedTrack, PagingObject<SavedTrack>> {
        return toPagingObjectAction(Supplier {
            get(
                EndpointBuilder("/me/tracks").with("limit", limit).with("offset", offset).with("market", market?.code)
                    .toString()
            ).toPagingObject(endpoint = this, serializer = SavedTrack.serializer())
        })
    }

    /**
     * Get a list of the albums saved in the current Spotify user’s ‘Your Music’ library.
     *
     * @return Paging Object of [SavedAlbum] ordered by position in library
     */
    fun getSavedAlbums(
        limit: Int? = null,
        offset: Int? = null,
        market: Market? = null
    ): SpotifyRestPagingAction<SavedAlbum, PagingObject<SavedAlbum>> {
        return toPagingObjectAction(Supplier {
            get(
                EndpointBuilder("/me/albums").with("limit", limit).with("offset", offset).with("market", market?.code)
                    .toString()
            ).toPagingObject(endpoint = this, serializer = SavedAlbum.serializer())
        })
    }

    /**
     * Check if the [LibraryType] with id [id] is already saved in the current Spotify user’s ‘Your Music’ library.
     *
     * @throws BadRequestException if [track] is not found
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
            get(
                EndpointBuilder("/me/$type/contains").with("ids", ids.joinToString(",") { type.id(it).encode() })
                    .toString()
            ).toObject(api, ArrayListSerializer(BooleanSerializer)).toList()
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
            put(EndpointBuilder("/me/$type").with("ids", ids.joinToString(",") { type.id(it).encode() }).toString())
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
            delete(EndpointBuilder("/me/$type").with("ids", ids.joinToString(",") { type.id(it).encode() }).toString())
            Unit
        })
    }
}

enum class LibraryType(private val value: String, internal val id: (String) -> String) {
    TRACK("tracks", { TrackURI(it).id }), ALBUM("albums", { AlbumURI(it).id });

    override fun toString() = value
}
