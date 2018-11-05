package com.adamratzman.spotify.utils

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyRestAction
import com.google.gson.Gson
import org.json.JSONObject
import java.io.InvalidObjectException
import java.net.URLEncoder
import java.util.*
import java.util.function.Supplier

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

    inline fun <reified T> getAll(): SpotifyRestAction<List<T>> = endpoint.toAction(
            Supplier {
                val pagingObjects = mutableListOf<PagingObject<T>>()
                var prev = previous?.let { getPrevious<T>().complete() }
                while (prev != null) {
                    pagingObjects.add(prev)
                    prev = prev.previous?.let { prev!!.getPrevious<T>().complete() }
                }
                pagingObjects.reverse() // closer we are to current, the further we are from the start
                var nxt = next?.let { getNext<T>().complete() }
                while (nxt != null) {
                    pagingObjects.add(nxt)
                    nxt = nxt.next?.let { nxt!!.getNext<T>().complete() }
                }
                // we don't need to reverse here, as it's in order
                pagingObjects.map { it.items }.flatten()
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
        null
    }
}