package com.adamratzman.obj

import com.adamratzman.main.SpotifyAPI
import com.adamratzman.main.gson
import org.jsoup.Connection
import org.jsoup.Jsoup

abstract class SpotifyEndpoint(val api: SpotifyAPI) {
    fun get(url: String): String {
        return execute(url)
    }

    fun put(url: String): String {
        return execute(url, Connection.Method.PUT)
    }

    fun delete(url: String): String {
        return execute(url, Connection.Method.DELETE)
    }

    private fun execute(url: String, method: Connection.Method = Connection.Method.GET): String {
        var connection = Jsoup.connect(url).ignoreContentType(true)
        if (api.token != null) connection = connection.header("Authorization", "Bearer ${api.token?.access_token}")
        val document = connection.ignoreHttpErrors(true).method(method).execute()
        if (document.statusCode() / 200 != 1 /* Check if status is 2xx */) throw BadRequestException(gson.fromJson(document.body(), ErrorResponse::class.java).error)
        return document.body()
    }
}


enum class Market(var code: String) {
    US("US")
}

data class CursorBasedPagingObject<out T>(val href:String,val items: List<T>, val limit: Int, val next: String?, val cursors: Cursor,
                                          val total: Int)
data class Cursor(val after: String)
data class PagingObject<out T>(val href: String, val items: List<T>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class LinkedResult<out T>(val href: String, val items: List<T>)
data class ArtistList(val artists: List<Artist>)
data class ArtistPNList(val artists: List<Artist?>)
data class TrackList(val tracks: List<Track>)

data class FeaturedPlaylists(val message: String?, val playlists: PagingObject<Playlist>)
data class PlaylistTrackPagingObject(val href: String, val items: List<PlaylistTrack>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class SimpleTrackPagingObject(val href: String, val items: List<SimpleTrack>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class AudioFeaturesResponse(val audio_features: List<AudioFeatures>)
data class TracksResponse(val tracks: List<Track?>)
data class AlbumsResponse(val albums: List<Album?>)