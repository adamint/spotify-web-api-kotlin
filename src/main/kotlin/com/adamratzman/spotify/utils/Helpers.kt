package com.adamratzman.spotify.utils

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyClientAPI
import com.adamratzman.spotify.main.base
import com.google.gson.Gson
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.InvalidObjectException
import java.net.URLEncoder
import java.util.*
import java.util.function.Supplier

abstract class SpotifyEndpoint(val api: SpotifyAPI) {
    fun get(url: String): String {
        return execute(url)
    }

    fun post(url: String, body: String? = null): String {
        return execute(url, body, Connection.Method.POST)
    }

    fun put(url: String, body: String? = null, contentType: String? = null): String {
        return execute(url, body, Connection.Method.PUT, contentType = contentType)
    }

    fun delete(url: String, body: String? = null, data: List<Pair<String, String>>? = null, contentType: String? = null): String {
        return execute(url, body, Connection.Method.DELETE, data = data, contentType = contentType)
    }

    private fun execute(url: String, body: String? = null, method: Connection.Method = Connection.Method.GET,
                        retry202: Boolean = true, contentType: String? = null, data: List<Pair<String, String>>? = null): String {
        if (api !is SpotifyClientAPI && System.currentTimeMillis() >= api.expireTime) {
            api.refreshClient()
            api.expireTime = System.currentTimeMillis() + api.token.expires_in * 1000
        }
        var connection = Jsoup.connect(url).ignoreContentType(true)
        data?.forEach { connection.data(it.first, it.second) }
        if (contentType != null) connection.header("Content-Type", contentType)
        if (body != null) {
            if (contentType != null) connection.requestBody(body)
            else
                connection = if (method == Connection.Method.DELETE) {
                    val key = JSONObject(body).keySet().toList()[0]
                    connection.data(key, JSONObject(body).getJSONArray(key).toString())
                } else connection.requestBody(body)
        }
        connection = connection.header("Authorization", "Bearer ${api.token.access_token}")
        val document = connection.method(method).ignoreHttpErrors(true).execute()
        if (document.statusCode() / 200 != 1 /* Check if status is 2xx */) throw BadRequestException(api.gson.fromJson(document.body(), ErrorResponse::class.java).error)
        else if (document.statusCode() == 202 && retry202) return execute(url, body, method, false)
        return document.body()
    }

    fun <T> toAction(supplier: Supplier<T>) = SpotifyRestAction(api, supplier)
}

internal class EndpointBuilder(val path: String) {
    val builder = StringBuilder(base)

    init {
        builder.append(path)
    }

    fun with(key: String, value: Any?): EndpointBuilder {
        if (value != null && (value !is String || value.isNotEmpty())) {
            if (builder.toString() == base + path) builder.append("?")
            else builder.append("&")
            builder.append(key).append("=").append(value.toString())
        }
        return this
    }

    fun build() = builder.toString()
}

data class CursorBasedPagingObject<out T>(val href: String, val items: List<T>, val limit: Int, val next: String?, val cursors: Cursor,
                                          val total: Int, val endpoint: SpotifyEndpoint) {
    inline fun <reified T> getNext(): SpotifyRestAction<CursorBasedPagingObject<T>> = endpoint.toAction(
            Supplier {
                next?.let { endpoint.get(it).toCursorBasedPagingObject<T>(endpoint = endpoint) }
                        ?: throw IllegalStateException("PagingObject#next is null!")
            })
}

data class Cursor(val after: String)
data class PagingObject<out T>(val href: String, val items: List<T>, val limit: Int, val next: String? = null, val offset: Int = 0,
                               val previous: String? = null, val total: Int, val endpoint: SpotifyEndpoint) {
    inline fun <reified T> getNext(): SpotifyRestAction<PagingObject<T>> = endpoint.toAction(
            Supplier {
                next?.let { endpoint.get(it).toPagingObject<T>(endpoint = endpoint) }
                        ?: throw IllegalStateException("PagingObject#next is null!")
            })

    inline fun <reified T> getPrevious(): SpotifyRestAction<PagingObject<T>> = endpoint.toAction(
            Supplier {
                previous?.let { endpoint.get(it).toPagingObject<T>(endpoint = endpoint) }
                        ?: throw IllegalStateException("PagingObject#previous is null!")
            })
}

data class LinkedResult<out T>(val href: String, val items: List<T>) {
    fun toPlaylistParams(): PlaylistParams {
        if (href.startsWith("https://api.spotify.com/v1/users/")) {
            val split = href.removePrefix("https://api.spotify.com/v1/users/").split("/playlists/")
            if (split.size == 2) return PlaylistParams(split[0], split[1].split("/")[0])
        }
        throw InvalidObjectException("This object is not linked to a playlist")
    }

    fun getArtistId(): String {
        if (href.startsWith("https://api.spotify.com/v1/artists/")) {
            return href.removePrefix("https://api.spotify.com/v1/artists/").split("/")[0]
        }
        throw InvalidObjectException("This object is not linked to an artist")
    }

    fun getAlbumId(): String {
        if (href.startsWith("https://api.spotify.com/v1/albums/")) {
            return href.removePrefix("https://api.spotify.com/v1/albums/").split("/")[0]
        }
        throw InvalidObjectException("This object is not linked to an album")
    }
}

data class PlaylistParams(val author: String, val id: String)

abstract class RelinkingAvailableResponse(val linkedTrack: LinkedTrack?) {
    fun isRelinked() = linkedTrack != null
}

internal fun String.byteEncode(): String {
    return String(Base64.getEncoder().encode(toByteArray()))
}

internal fun String.encode() = URLEncoder.encode(this, "UTF-8")!!

inline fun <reified T> Any.toObject(o: Any): T {
    return ((o as? SpotifyAPI)?.gson ?: (o as? Gson)
    ?: throw IllegalArgumentException("Parameter must be a SpotifyAPI or Gson instance"))
            .fromJson(this as String, T::class.java)
}

inline fun <reified T> String.toPagingObject(innerObjectName: String? = null, endpoint: SpotifyEndpoint): PagingObject<T> {
    val jsonObject = if (innerObjectName != null) JSONObject(this).getJSONObject(innerObjectName) else JSONObject(this)
    return PagingObject(
            jsonObject.getString("href"),
            jsonObject.getJSONArray("items").map { it.toString().toObject<T>(endpoint.api) },
            jsonObject.getInt("limit"),
            jsonObject.get("next") as? String,
            jsonObject.get("offset") as Int,
            jsonObject.get("previous") as? String,
            jsonObject.getInt("total"),
            endpoint)
}

inline fun <reified T> String.toCursorBasedPagingObject(innerObjectName: String? = null, endpoint: SpotifyEndpoint): CursorBasedPagingObject<T> {
    val jsonObject = if (innerObjectName != null) JSONObject(this).getJSONObject(innerObjectName) else JSONObject(this)
    return CursorBasedPagingObject(
            jsonObject.getString("href"),
            jsonObject.getJSONArray("items").map { it.toString().toObject<T>(endpoint) },
            jsonObject.getInt("limit"),
            jsonObject.get("next") as? String,
            endpoint.api.gson.fromJson(jsonObject.getJSONObject("cursors").toString(), Cursor::class.java),
            if (jsonObject.keySet().contains("total")) jsonObject.getInt("total") else -1,
            endpoint)
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

fun <T> catch(function: () -> T): T? {
    return try {
        function()
    } catch (e: BadRequestException) {
        if (e.error.status == 404) null else throw e
    }
}