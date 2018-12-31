/* Created by Adam Ratzman (2018) */

package com.adamratzman.spotify.utils

import com.adamratzman.spotify.main.SpotifyAPI
import com.adamratzman.spotify.main.SpotifyException
import com.adamratzman.spotify.main.SpotifyRestAction
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JSON
import org.json.JSONArray
import org.json.JSONObject
import java.io.InvalidObjectException
import java.net.URLEncoder
import java.util.Base64
import java.util.function.Supplier

@Serializable
data class Cursor(val after: String)

class CursorBasedPagingObject<T>(
    href: String,
    items: List<T>,
    limit: Int,
    next: String?,
    val cursors: Cursor,
    total: Int,
    endpoint: SpotifyEndpoint,
    serializer: KSerializer<T>
) : PagingObject<T>(href, items, limit, next, 0, null, total, endpoint, serializer)

@Serializable
open class PagingObject<T>(
    val href: String,
    val items: List<T>,
    val limit: Int,
    val next: String? = null,
    val offset: Int = 0,
    val previous: String? = null,
    val total: Int,
    var endpoint: SpotifyEndpoint,
    val serializer: KSerializer<T>
) {
    fun getNext(): SpotifyRestAction<PagingObject<T>?> = endpoint.toAction(
        Supplier {
            catch {
                if (this is CursorBasedPagingObject) next?.let {
                    endpoint.get(it).toCursorBasedPagingObject(serializer = serializer, endpoint = endpoint)
                }
                else next?.let { endpoint.get(it).toPagingObject<T>(serializer = serializer, endpoint = endpoint) }
            }
        })

    fun getPrevious(): SpotifyRestAction<PagingObject<T>?> = endpoint.toAction(
        Supplier {
            catch {
                previous?.let { endpoint.get(it).toPagingObject(serializer = serializer, endpoint = endpoint) }
            }
        })

    fun getAll(): SpotifyRestAction<List<PagingObject<T>>> {
        return endpoint.toAction(
            Supplier {
                if (this is CursorBasedPagingObject) {
                    val pagingObjects = mutableListOf(this)
                    var next = getNext().complete()
                    while (next != null) {
                        pagingObjects.add(next as CursorBasedPagingObject<T>)
                        next = getNext().complete()
                    }
                    pagingObjects.toList()
                } else {
                    val pagingObjects = mutableListOf<PagingObject<T>>()
                    var prev = previous?.let { getPrevious().complete() }
                    while (prev != null) {
                        pagingObjects.add(prev)
                        prev = prev.previous?.let { prev?.getPrevious()?.complete() }
                    }
                    pagingObjects.reverse() // closer we are to current, the further we are from the start

                    pagingObjects.add(this)

                    var nxt = next?.let { getNext().complete() }
                    while (nxt != null) {
                        pagingObjects.add(nxt)
                        nxt = nxt.next?.let { nxt?.getNext()?.complete() }
                    }
                    // we don't need to reverse here, as it's in order
                    pagingObjects.toList()
                }
            })
    }

    fun getAllItems(): SpotifyRestAction<List<T>> {
        return endpoint.toAction(Supplier {
            getAll().complete().asSequence().map { it.items }.toList().flatten()
        })
    }
}

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

@Serializable
abstract class RelinkingAvailableResponse(@Transient val linkedTrack: LinkedTrack? = null) : Linkable() {
    fun isRelinked() = linkedTrack != null
}

internal fun String.byteEncode(): String {
    return String(Base64.getEncoder().encode(toByteArray()))
}

internal fun String.encode() = URLEncoder.encode(this, "UTF-8")!!

internal fun <T> String.toObjectNullable(o: SpotifyAPI?, serializer: KSerializer<T>): T? = try {
    toObject<T>(o, serializer)
} catch (e: Exception) {
    null
}

@Suppress("UNCHECKED_CAST")
internal fun <T> String.toObject(o: SpotifyAPI?, serializer: KSerializer<T>): T {
    try {
        val obj = (JSON.nonstrict.parse(serializer, this) as? T) ?: throw SpotifyException(
            "Unable to parse $this",
            IllegalArgumentException("$serializer not found")
        )
        o?.let {
            if (obj is Linkable) obj.api = o
            obj.instantiatePagingObjects(o)
        }
        return obj
    } catch (e: java.lang.Exception) {
        println(this)
        throw e
    }
}

internal fun Any.instantiatePagingObjects(spotifyAPI: SpotifyAPI) = when {
    this is FeaturedPlaylists -> this.playlists
    this is Album -> this.tracks
    this is Playlist -> this.tracks
    else -> null
}.let { it?.endpoint = spotifyAPI.tracks; this }

internal fun <T> String.toPagingObject(
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint,
    serializer: KSerializer<T>
): PagingObject<T> {
    val jsonObject = if (innerObjectName != null) JSONObject(this).getJSONObject(innerObjectName) else JSONObject(this)
    val pagingObject = PagingObject(
        jsonObject.getString("href"),
        jsonObject.getJSONArray("items").map { it.toString().toObject<T>(endpoint.api, serializer) },
        jsonObject.getInt("limit"),
        jsonObject.get("next") as? String,
        jsonObject.get("offset") as Int,
        jsonObject.get("previous") as? String,
        jsonObject.getInt("total"),
        endpoint,
        serializer
    )
    return pagingObject
}

internal fun <T> String.toCursorBasedPagingObject(
    innerObjectName: String? = null,
    endpoint: SpotifyEndpoint,
    serializer: KSerializer<T>
): CursorBasedPagingObject<T> {
    val jsonObject = if (innerObjectName != null) JSONObject(this).getJSONObject(innerObjectName) else JSONObject(this)
    val cursorBasedPagingObject = CursorBasedPagingObject(
        jsonObject.getString("href"),
        jsonObject.getJSONArray("items").map { it.toString().toObject<T>(endpoint.api, serializer) },
        jsonObject.getInt("limit"),
        jsonObject.get("next") as? String,
        JSON.parse(Cursor.serializer(), jsonObject.getJSONObject("cursors").toString()),
        if (jsonObject.keySet().contains("total")) jsonObject.getInt("total") else -1,
        endpoint,
        serializer
    )
    return cursorBasedPagingObject
}

internal fun <T> String.toLinkedResult(api: SpotifyAPI, serializer: KSerializer<T>): LinkedResult<T> {
    val jsonObject = JSONObject(this)
    return LinkedResult(
        jsonObject.getString("href"),
        jsonObject.getJSONArray("items").map { it.toString().toObject(api, serializer) })
}

internal fun <T> String.toInnerObject(innerName: String, api: SpotifyAPI, serializer: KSerializer<T>): T {
    return JSONObject(this).let { it.optJSONObject(innerName) ?: it.getJSONArray(innerName) }
        .toString().toObject(api, serializer)
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
