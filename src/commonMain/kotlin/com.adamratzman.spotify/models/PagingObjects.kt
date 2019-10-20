/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.models.serialization.toCursorBasedPagingObject
import com.adamratzman.spotify.models.serialization.toPagingObject
import com.adamratzman.spotify.utils.catch
import com.squareup.moshi.Json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.function.Supplier
import kotlin.reflect.KClass

/*
    Types used in PagingObjects and CursorBasedPagingObjects:

    CursorBasedPagingObject:
       PlayHistory
       Artist

    PagingObject:
       SimpleTrack
       SimpleAlbum
       SpotifyCategory
       SimplePlaylist
       SavedTrack
       SavedAlbum
       Artist
       Track
       PlaylistTrack

 */

enum class PagingTraversalType {
    BACKWARDS,
    FORWARDS;
}

/**
 * The offset-based paging object is a container for a set of objects. It contains a key called items
 * (whose value is an array of the requested objects) along with other keys like previous, next and
 * limit that can be useful in future calls.
 *
 * @property href A link to the Web API endpoint returning the full result of the request.
 * @property items The requested data.
 * @property limit The maximum number of items in the response (as set in the query or by default).
 * @property next URL to the next page of items. ( null if none)
 * @property previous URL to the previous page of items. ( null if none)
 * @property total The maximum number of items available to return.
 * @property offset The offset of the items returned (as set in the query or by default).
 */
@Serializable
class PagingObject<T : Any>(
    val _href: String,
    val _items: List<T>,
    val _limit: Int,
    val _next: String?,
    val _offset: Int,
    val _previous: String?,
    val _total: Int
) : AbstractPagingObject<T>(_href, _items, _limit, _next, _offset, _previous, _total) {
    /**
     * Get the next set of [T] items
     */
    fun getNext() = endpoint!!.toAction( {
        catch {
            getImpl(PagingTraversalType.FORWARDS) as? PagingObject<T>
        }
    })

    /**
     * Get the previous set of [T] items
     */
    fun getPrevious() = endpoint!!.toAction(Supplier {
        catch {
            getImpl(PagingTraversalType.BACKWARDS) as? PagingObject<T>
        }
    })

    @Suppress("UNCHECKED_CAST")
    override fun getImpl(type: PagingTraversalType): AbstractPagingObject<T>? {
        return (if (type == PagingTraversalType.FORWARDS) next else previous)?.let { endpoint.get(it) }?.let { json ->
            when (itemClazz) {
                SimpleTrack::class -> json.toPagingObject<SimpleTrack>(null, endpoint)
                SpotifyCategory::class -> json.toPagingObject<SpotifyCategory>(null, endpoint)
                SimpleAlbum::class -> json.toPagingObject<SimpleAlbum>(null, endpoint)
                SimplePlaylist::class -> json.toPagingObject<SimplePlaylist>(null, endpoint)
                SavedTrack::class -> json.toPagingObject<SavedTrack>(null, endpoint)
                SavedAlbum::class -> json.toPagingObject<SavedAlbum>(null, endpoint)
                Artist::class -> json.toPagingObject<Artist>(null, endpoint)
                Track::class -> json.toPagingObject<Track>(null, endpoint)
                PlaylistTrack::class -> json.toPagingObject<PlaylistTrack>(null, endpoint)
                else -> throw IllegalArgumentException("Unknown type in $href response")
            } as? PagingObject<T>
        }
    }

    override fun getAllImpl(): Sequence<AbstractPagingObject<T>> {
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
        return pagingObjects.asSequence()
    }

    /**
     * Get all PagingObjects associated with the request
     */
    @Suppress("UNCHECKED_CAST")
    fun getAll() = endpoint!!.toAction(Supplier { (getAllImpl() as Sequence<PagingObject<T>>).toList() })

    /**
     * Get all items of type [T] associated with the request
     */
    fun getAllItems() = endpoint!!.toAction(Supplier { getAll().complete().map { it.items }.flatten() })
}

/**
 * The cursor-based paging object is a container for a set of objects. It contains a key called
 * items (whose value is an array of the requested objects) along with other keys like next and
 * cursors that can be useful in future calls.
 *
 * @property href A link to the Web API endpoint returning the full result of the request.
 * @property items The requested data.
 * @property limit The maximum number of items in the response (as set in the query or by default).
 * @property next URL to the next page of items. ( null if none)
 * @property total The maximum number of items available to return.
 * @property cursor The cursors used to find the next set of items..
 */
@Serializable
class CursorBasedPagingObject<T : Any>(
    val _href: String,
    val _items: List<T>,
    val _limit: Int,
    val _next: String?,
    @SerialName("cursors") val cursor: Cursor,
    val _total: Int
) : AbstractPagingObject<T>(_href, _items, _limit, _next, 0, null, _total) {
    /**
     * Get the next set of [T] items
     */
    fun getNext() = endpoint!!.toAction(Supplier {
        catch {
            getImpl(PagingTraversalType.FORWARDS) as? CursorBasedPagingObject<T>
        }
    })

    /**
     * Get all CursorBasedPagingObjects associated with the request
     */
    @Suppress("UNCHECKED_CAST")
    fun getAll() = endpoint!!.toAction(Supplier {
        getAllImpl() as Sequence<CursorBasedPagingObject<T>>
    })

    /**
     * Get all items of type [T] associated with the request
     */
    fun getAllItems() = endpoint!!.toAction(Supplier {
        getAll().complete().map { it.items }.flatten().toList()
    })

    @Suppress("UNCHECKED_CAST")
    override fun getImpl(type: PagingTraversalType): AbstractPagingObject<T>? {
        if (type == PagingTraversalType.BACKWARDS) {
            throw IllegalArgumentException("CursorBasedPagingObjects only can go forwards")
        }
        return next?.let {
            val url = endpoint!!.get(it)
            when (itemClazz) {
                PlayHistory::class -> url.toCursorBasedPagingObject<PlayHistory>(
                        null,
                        endpoint!!
                )
                Artist::class -> url.toCursorBasedPagingObject<Artist>(
                        null,
                        endpoint!!
                )
                else -> throw IllegalArgumentException("Unknown type in $href")
            } as? CursorBasedPagingObject<T>
        }
    }

    override fun getAllImpl(): Sequence<AbstractPagingObject<T>> {
        return generateSequence(this) { it.getImpl(PagingTraversalType.FORWARDS) as? CursorBasedPagingObject<T> }
    }
}

/**
 * The cursor to use as key to find the next (or previous) page of items.
 *
 * @property before The cursor to use as key to find the previous page of items.
 * @property after The cursor to use as key to find the next page of items.
 */
@Serializable
data class Cursor(val before: String? = null, val after: String? = null)

/**
 * @property href A link to the Web API endpoint returning the full result of the request.
 * @property items The requested data.
 * @property limit The maximum number of items in the response (as set in the query or by default).
 * @property next URL to the next page of items. ( null if none)
 * @property previous URL to the previous page of items. ( null if none)
 * @property total The maximum number of items available to return.
 * @property offset The offset of the items returned (as set in the query or by default).
 */
@Serializable
abstract class AbstractPagingObject<T : Any>(
    val href: String,
    val items: List<T>,
    val limit: Int,
    val next: String? = null,
    val offset: Int = 0,
    val previous: String? = null,
    val total: Int
) {
    @Transient
    internal var endpoint: SpotifyEndpoint? = null

    @Transient
    internal var itemClazz: KClass<T>? = null

    internal abstract fun getImpl(type: PagingTraversalType): AbstractPagingObject<T>?
    internal abstract fun getAllImpl(): Sequence<AbstractPagingObject<T>>

    internal fun getNextImpl() = getImpl(PagingTraversalType.FORWARDS)
    internal fun getPreviousImpl() = getImpl(PagingTraversalType.BACKWARDS)
}

internal fun Any.instantiatePagingObjects(spotifyAPI: SpotifyAPI) = when {
    this is FeaturedPlaylists -> this.playlists
    this is Album -> this.tracks
    this is Playlist -> this.tracks
    else -> null
}.let { it?.endpoint = spotifyAPI.tracks; this }
