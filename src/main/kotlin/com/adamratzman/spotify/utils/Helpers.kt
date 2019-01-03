/* Created by Adam Ratzman (2018) */

package com.adamratzman.spotify.utils

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyException
import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import org.json.JSONArray
import org.json.JSONObject
import java.io.InvalidObjectException
import java.net.URLEncoder
import java.util.Base64

data class Cursor(val after: String?)

data class LinkedResult<out T>(val href: String, val items: List<T>) {
    fun toPlaylist(): PlaylistURI {
        if (href.startsWith("https://api.spotify.com/v1/users/")) {
            val split = href.removePrefix("https://api.spotify.com/v1/users/").split("/playlists/")
            if (split.size == 2) return PlaylistURI(split[1].split("/")[0])
        }
        throw InvalidObjectException("This object is not linked to a playlist")
    }

    fun getArtist(): ArtistURI {
        if (href.startsWith("https://api.spotify.com/v1/artists/")) {
            return ArtistURI(href.removePrefix("https://api.spotify.com/v1/artists/").split("/")[0])
        }
        throw InvalidObjectException("This object is not linked to an artist")
    }

    fun getAlbum(): AlbumURI {
        if (href.startsWith("https://api.spotify.com/v1/albums/")) {
            return AlbumURI(href.removePrefix("https://api.spotify.com/v1/albums/").split("/")[0])
        }
        throw InvalidObjectException("This object is not linked to an album")
    }
}

abstract class RelinkingAvailableResponse(@Json(ignored = true) val linkedTrack: LinkedTrack? = null) : Linkable() {
    fun isRelinked() = linkedTrack != null
}

internal fun String.byteEncode(): String {
    return String(Base64.getEncoder().encode(toByteArray()))
}

internal fun String.encode() = URLEncoder.encode(this, "UTF-8")!!

internal inline fun <reified T> String.toObjectNullable(o: SpotifyAPI?): T? = try {
    toObject<T>(o)
} catch (e: Exception) {
    null
}

internal inline fun <reified T> String.toObject(o: SpotifyAPI?): T {
    try {
        val klaxon = o?.klaxon ?: Klaxon()
        val obj = klaxon.parse<T>(this) ?: throw SpotifyException(
            "Unable to parse $this",
            IllegalArgumentException("$this not found")
        )
        o?.let {
            if (obj is Linkable) obj.api = o
            if (obj is AbstractPagingObject<*>) obj.endpoint = o.tracks
            obj.instantiatePagingObjects(o)
        }
        return obj
    } catch (e: java.lang.Exception) {
        println(this)
        throw e
    }
}

internal inline fun <reified T> String.toArray(o: SpotifyAPI?): List<T> {
    val klaxon = o?.klaxon ?: Klaxon()
    return klaxon.parseArray<T>(this) ?: throw SpotifyException(
        "Unable to parse $this",
        IllegalArgumentException("$this not found")
    )
}

internal inline fun <reified T> String.toPagingObject(
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint
): PagingObject<T> {
    val jsonObject = if (innerObjectName != null) JSONObject(this).getJSONObject(innerObjectName) else JSONObject(this)
    return PagingObject(
        jsonObject.getString("href"),
        jsonObject.getJSONArray("items").map { it.toString().toObject<T>(endpoint.api) },
        jsonObject.getInt("limit"),
        jsonObject.get("next") as? String,
        jsonObject.get("offset") as Int,
        jsonObject.get("previous") as? String,
        jsonObject.getInt("total")
    ).apply {
        this.endpoint = endpoint
        this.itemClazz = T::class.java
    }
}

internal inline fun <reified T> String.toCursorBasedPagingObject(
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint
): CursorBasedPagingObject<T> {
    val jsonObject = if (innerObjectName != null) JSONObject(this).getJSONObject(innerObjectName) else JSONObject(this)
    return CursorBasedPagingObject(
        jsonObject.getString("href"),
        jsonObject.getJSONArray("items").map { it.toString().toObject<T>(endpoint.api) },
        jsonObject.getInt("limit"),
        jsonObject.get("next") as? String,
        jsonObject.getJSONObject("cursors").toString().toObject(endpoint.api),
        if (jsonObject.keySet().contains("total")) jsonObject.getInt("total") else -1
    ).apply {
        this.endpoint = endpoint
        this.itemClazz = T::class.java
    }
}

internal inline fun <reified T> String.toLinkedResult(api: SpotifyAPI): LinkedResult<T> {
    val jsonObject = JSONObject(this)
    return LinkedResult(
        jsonObject.getString("href"),
        jsonObject.getJSONArray("items").map { it.toString().toObject<T>(api) })
}

internal inline fun <reified T> String.toInnerObject(innerName: String, api: SpotifyAPI): T {
    return JSONObject(this).let { it.optJSONObject(innerName) ?: it.getJSONArray(innerName) }
        .toString().toObject(api)
}

internal inline fun <reified T> String.toInnerArray(innerName: String, api: SpotifyAPI): List<T> {
    return JSONObject(this).let { it.optJSONObject(innerName) ?: it.getJSONArray(innerName) }
        .toString().toArray(api)
}

internal fun <T> catch(function: () -> T): T? {
    return try {
        function()
    } catch (e: BadRequestException) {
        null
    }
}

internal inline fun <R> JSONArray.map(transform: (Any) -> R): List<R> {
    return List(this.length()) {
        transform(this.get(it))
    }
}
