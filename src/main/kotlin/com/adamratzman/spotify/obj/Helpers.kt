package com.adamratzman.spotify.obj

import com.adamratzman.spotify.main.SpotifyAPI
import com.google.gson.Gson
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.util.*

abstract class SpotifyEndpoint(val api: SpotifyAPI) {
    fun get(url: String): String {
        return execute(url)
    }

    fun post(url: String, body: String? = null): String {
        return execute(url, body, Connection.Method.POST)
    }

    fun put(url: String, body: String? = null): String {
        return execute(url, body, Connection.Method.PUT)
    }

    fun delete(url: String, body: String? = null): String {
        return execute(url, body, Connection.Method.DELETE)
    }

    private fun execute(url: String, body: String? = null, method: Connection.Method = Connection.Method.GET, retry202: Boolean = true): String {
        var connection = Jsoup.connect(url).ignoreContentType(true)
        if (body != null) {
            if (method == Connection.Method.DELETE) {
                val key = JSONObject(body).keySet().toList()[0]
                connection = connection.data(key, JSONObject(body).getJSONArray(key).toString())
            } else connection = connection.requestBody(body)
        }
        if (api.token != null) connection = connection.header("Authorization", "Bearer ${api.token?.access_token}")
        val document = connection.ignoreHttpErrors(true).method(method).execute()
        if (document.statusCode() / 200 != 1 /* Check if status is 2xx */) api.logger.logError(false, null, BadRequestException(api.gson.fromJson(document.body(), ErrorResponse::class.java).error))
        else if (document.statusCode() == 202 && retry202) return execute(url, body, method, false)
        return document.body()
    }
}


enum class Market(var code: String) {
    US("US")
}

data class CursorBasedPagingObject<out T>(val href: String, val items: List<T>, val limit: Int, val next: String?, val cursors: Cursor,
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


fun String.encode(): String {
    return String(Base64.getEncoder().encode(toByteArray()))
}

inline fun <reified T> Any.toObject(o: Any): T {
    return ((o as? SpotifyAPI)?.gson ?: (o as? Gson)
    ?: throw IllegalArgumentException("Parameter must be a SpotifyAPI or Gson instance"))
            .fromJson(this as String, T::class.java)
}

inline fun <reified T> String.toPagingObject(innerObjectName: String? = null, api: SpotifyAPI): PagingObject<T> {
    val jsonObject = if (innerObjectName != null) JSONObject(this).getJSONObject(innerObjectName) else JSONObject(this)
    return PagingObject(
            jsonObject.getString("href"),
            jsonObject.getJSONArray("items").map { it.toString().toObject<T>(api) },
            jsonObject.getInt("limit"),
            jsonObject.get("next") as? String,
            jsonObject.get("offset") as Int,
            jsonObject.get("previous") as? String,
            jsonObject.getInt("total"))
}

inline fun <reified T> String.toCursorBasedPagingObject(innerObjectName: String? = null, api: SpotifyAPI): CursorBasedPagingObject<T> {
    val jsonObject = if (innerObjectName != null) JSONObject(this).getJSONObject(innerObjectName) else JSONObject(this)
    return CursorBasedPagingObject(
            jsonObject.getString("href"),
            jsonObject.getJSONArray("items").map { it.toString().toObject<T>(api) },
            jsonObject.getInt("limit"),
            jsonObject.get("next") as? String,
            api.gson.fromJson(jsonObject.getJSONObject("cursors").toString(), Cursor::class.java),
            if (jsonObject.keySet().contains("total")) jsonObject.getInt("total") else -1)
}

inline fun <reified T> String.toLinkedResult(api: SpotifyAPI): LinkedResult<T> {
    val jsonObject = JSONObject(this)
    return LinkedResult(
            jsonObject.getString("href"),
            jsonObject.getJSONArray("items").map { it.toString().toObject<T>(api) })
}

inline fun <reified T> String.toInnerObject(innerName: String, api: SpotifyAPI): List<T> {
    return JSONObject(this).getJSONArray(innerName).map { it.toString().toObject<T>(api) }
}