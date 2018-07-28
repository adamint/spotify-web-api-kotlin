package com.adamratzman.spotify.endpoints.priv.library

import com.adamratzman.spotify.main.SpotifyAPI
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
    fun getSavedTracks(limit: Int = 20, offset: Int = 0, market: Market? = null): SpotifyRestAction<PagingObject<SavedTrack>> {
        return toAction(Supplier {
            get(EndpointBuilder("/me/tracks").build()).toPagingObject<SavedTrack>(endpoint = this)
        })
    }

    /**
     * Get a list of the albums saved in the current Spotify user’s ‘Your Music’ library.
     *
     * @return Paging Object of [SavedAlbum] ordered by position in library
     */
    fun getSavedAlbums(): SpotifyRestAction<PagingObject<SavedAlbum>> {
        return toAction(Supplier {
            get(EndpointBuilder("/me/albums").build()).toPagingObject<SavedAlbum>(endpoint = this)
        })
    }

    /**
     * Check if a track is already saved in the current Spotify user’s ‘Your Music’ library.
     *
     * @throws BadRequestException if [id] is not found
     */
    fun doesLibraryContainTrack(id: String): SpotifyRestAction<Boolean> {
        return toAction(Supplier {
            doesLibraryContainTracks(id).complete()[0]
        })
    }

    /**
     * Check if a track is already saved in the current Spotify user’s ‘Your Music’ library.
     *
     * @throws BadRequestException if a provided id is not found
     */
    fun doesLibraryContainTracks(vararg ids: String): SpotifyRestAction<List<Boolean>> {
        return toAction(Supplier {
            get(EndpointBuilder("/me/tracks/contains").with("ids", ids.joinToString(",") { it.encode() })
                    .build()).toObject<List<Boolean>>(api)
        })
    }

    /**
     * Check if an album is already saved in the current Spotify user’s ‘Your Music’ library.
     *
     * @throws BadRequestException if id is not found
     */
    fun doesLibraryContainAlbum(id: String): SpotifyRestAction<Boolean> {
        return toAction(Supplier {
            doesLibraryContainAlbums(id).complete()[0]
        })
    }

    /**
     * Check if one or more albums is already saved in the current Spotify user’s ‘Your Music’ library.
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    fun doesLibraryContainAlbums(vararg ids: String): SpotifyRestAction<List<Boolean>> {
        return toAction(Supplier {
            get(EndpointBuilder("/me/albums/contains").with("ids", ids.joinToString(",") { it.encode() })
                    .build()).toObject<List<Boolean>>(api)
        })
    }

    /**
     * Save a track to the current user’s ‘Your Music’ library.
     *
     * @throws BadRequestException if the id is invalid
     */
    fun addTrackToLibrary(id: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            addTracksToLibrary(id).complete()
        })
    }

    /**
     * Save one or more tracks to the current user’s ‘Your Music’ library.
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    fun addTracksToLibrary(vararg ids: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put(EndpointBuilder("/me/tracks").with("ids", ids.joinToString(",") { it.encode() }).build())
            Unit
        })
    }

    /**
     * Save an album to the current user’s ‘Your Music’ library.
     *
     * @throws BadRequestException if the id is invalid
     */
    fun addAlbumToLibrary(id: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            addAlbumsToLibrary(id).complete()
        })
    }

    /**
     * Save one or more albums to the current user’s ‘Your Music’ library.
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    fun addAlbumsToLibrary(vararg ids: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put(EndpointBuilder("/me/albums").with("ids", ids.joinToString(",") { it.encode() }).build())
            Unit
        })
    }

    /**
     * Remove a track from the current user’s ‘Your Music’ library.
     *
     * @throws BadRequestException if the provided id is invalid
     */
    fun removeTrackFromLibrary(id: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            removeTracksFromLibrary(id).complete()
        })
    }

    /**
     * Remove one or more tracks from the current user’s ‘Your Music’ library.
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    fun removeTracksFromLibrary(vararg ids: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            delete(EndpointBuilder("/me/tracks").with("ids", ids.joinToString(",") { it.encode() }).build())
            Unit
        })
    }

    /**
     * Remove an album from the current user’s ‘Your Music’ library.
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    fun removeAlbumFromLibrary(id: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            removeAlbumsFromLibrary(id).complete()
        })
    }

    /**
     * Remove one or more albums from the current user’s ‘Your Music’ library.
     *
     * @throws BadRequestException if any of the provided ids is invalid
     */
    fun removeAlbumsFromLibrary(vararg ids: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            delete(EndpointBuilder("/me/albums").with("ids", ids.joinToString(",") { it.encode() }).build())
            Unit
        })
    }
}