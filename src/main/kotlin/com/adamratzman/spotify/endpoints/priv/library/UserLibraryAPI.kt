package com.adamratzman.spotify.endpoints.priv.library

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.utils.*
import java.util.function.Supplier

class UserLibraryAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getSavedTracks(): SpotifyRestAction<PagingObject<SavedTrack>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me/tracks").toPagingObject<SavedTrack>(api = api)
        })
    }

    fun getSavedAlbums(): SpotifyRestAction<PagingObject<SavedAlbum>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me/albums").toPagingObject<SavedAlbum>(api = api)
        })
    }

    fun savedTracksContains(vararg ids: String): SpotifyRestAction<List<Boolean>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me/tracks/contains?ids=${ids.joinToString(",") { it.encode() }}").toObject<List<Boolean>>(api)
        })
    }

    fun savedAlbumsContains(vararg ids: String): SpotifyRestAction<List<Boolean>> {
        return toAction(Supplier {
            get("https://api.spotify.com/v1/me/albums/contains?ids=${ids.joinToString(",") { it.encode() }}").toObject<List<Boolean>>(api)
        })
    }

    fun saveTracks(vararg ids: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put("https://api.spotify.com/v1/me/tracks?ids=${ids.joinToString(",") { it.encode() }}")
            Unit
        })
    }

    fun saveAlbums(vararg ids: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            put("https://api.spotify.com/v1/me/albums?ids=${ids.joinToString(",") { it.encode() }}")
            Unit
        })
    }

    fun removeSavedTracks(vararg ids: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            delete("https://api.spotify.com/v1/me/tracks?ids=${ids.joinToString(",") { it.encode() }}")
            Unit
        })
    }

    fun removeSavedAlbums(vararg ids: String): SpotifyRestAction<Unit> {
        return toAction(Supplier {
            delete("https://api.spotify.com/v1/me/albums?ids=${ids.joinToString(",") { it.encode() }}")
            Unit
        })
    }
}