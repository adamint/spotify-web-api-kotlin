package com.adamratzman.spotify.kotlin.endpoints.priv.library

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.obj.*

class UserLibraryAPI(api: SpotifyAPI) : SpotifyEndpoint(api) {
    fun getSavedTracks(): PagingObject<SavedTrack> {
        return get("https://api.spotify.com/v1/me/tracks").toPagingObject()
    }

    fun getSavedAlbums(): PagingObject<SavedAlbum> {
        return get("https://api.spotify.com/v1/me/albums").toPagingObject()
    }

    fun savedTracksContains(vararg ids: String): List<Boolean> {
        return get("https://api.spotify.com/v1/me/tracks/contains?ids=${ids.joinToString(",")}").toObject()
    }

    fun savedAlbumsContains(vararg ids: String): List<Boolean> {
        return get("https://api.spotify.com/v1/me/albums/contains?ids=${ids.joinToString(",")}").toObject()
    }

    fun saveTracks(vararg ids: String) {
        put("https://api.spotify.com/v1/me/tracks?ids=${ids.joinToString(",")}")
    }

    fun saveAlbums(vararg ids: String) {
        put("https://api.spotify.com/v1/me/albums?ids=${ids.joinToString(",")}")
    }

    fun removeSavedTracks(vararg ids: String) {
        delete("https://api.spotify.com/v1/me/tracks?ids=${ids.joinToString(",")}")
    }

    fun removeSavedAlbums(vararg ids: String) {
        delete("https://api.spotify.com/v1/me/albums?ids=${ids.joinToString(",")}")
    }
}