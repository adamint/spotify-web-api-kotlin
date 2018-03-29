package com.adamratzman.obj

import com.adamratzman.main.SpotifyAPI
import com.adamratzman.main.gson
import org.jsoup.Connection
import org.jsoup.Jsoup

abstract class Endpoint(val api: SpotifyAPI) {
    fun get(url: String): String {
        return execute(url)
    }

    fun put(url: String): String {
        return execute(url, Connection.Method.PUT)
    }

    fun delete(url: String): String {
        return execute(url, Connection.Method.DELETE)
    }

    fun execute(url: String, method: Connection.Method = Connection.Method.GET): String {
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

data class PagingObject<out T>(val href: String, val items: List<T>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class LinkedResult<out T>(val href: String, val items: List<T>)

data class AlbumQNList(val artists: List<Album?>)
data class ArtistList(val artists: List<Artist>)
data class ArtistPNList(val artists: List<Artist?>)
data class TrackList(val tracks: List<Track>)

data class SimpleTrackLinkedResult(val href: String, val items: List<SimpleTrack>)
data class SimpleAlbumLinkedResult(val href: String, val items: List<SimpleAlbum>)
data class PlaylistTrackLinkedResult(val href: String, val items: List<PlaylistTrack>)

data class MessageResult<out T>(val message: String?, val playlists: PlaylistPagingObject)

data class PlaylistPagingObject(val href: String, val items: List<Playlist>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class PlaylistTrackPagingObject(val href: String, val items: List<PlaylistTrack>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class SimpleTrackPagingObject(val href: String, val items: List<SimpleTrack>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class ArtistPagingObject(val href: String, val items: List<Artist>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class SimpleAlbumPagingObject(val href: String, val items: List<SimpleAlbum>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class SpotifyCategoryPagingObject(val href: String, val items: List<SpotifyCategory>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class AlbumPagingObject(val href: String, val items: List<Album>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class SimplePlaylistPagingObject(val href: String, val items: List<SimplePlaylist>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class SavedAlbumPagingObject(val href: String, val items: List<SavedAlbum>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)
data class SavedTrackPagingObject(val href: String, val items: List<SavedTrack>, val limit: Int, val next: String? = null, val offset: Int = 0, val previous: String? = null, val total: Int)

data class AudioFeaturesResponse(val audio_features: List<AudioFeatures>)
data class TracksResponse(val tracks: List<Track?>)
data class AlbumsResponse(val albums: List<Album?>)
data class AlbumPagingResponse(val albums: AlbumPagingObject)
data class SpotifyCategoryPagingResponse(val categories: SpotifyCategoryPagingObject)
data class SimplePlaylistPagingResponse(val playlists: SimplePlaylistPagingObject)
data class PlaylistPagingResponse(val playlists: PlaylistPagingObject)
data class ArtistPagingResponse(val artists: ArtistPagingObject)
data class SimpleAlbumPagingResponse(val albums: SimpleAlbumPagingObject)
data class SimpleTrackPagingResponse(val tracks: SimpleTrackPagingObject)