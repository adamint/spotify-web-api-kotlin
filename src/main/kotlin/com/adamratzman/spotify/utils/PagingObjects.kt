@file:Suppress("UNCHECKED_CAST")

package com.adamratzman.spotify.utils

import com.adamratzman.spotify.main.SpotifyAPI
import com.beust.klaxon.Json
import java.util.function.Supplier

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

enum class PagingTraversalType { BACKWARDS, FORWARDS }

class PagingObject<T>(
    href: String,
    items: List<T>,
    limit: Int,
    next: String?,
    offset: Int,
    previous: String?,
    total: Int
) : AbstractPagingObject<T>(href, items, limit, next, offset, previous, total) {
    fun getNext() = endpoint.toAction(Supplier {
        catch {
            getImpl(PagingTraversalType.FORWARDS) as? PagingObject<T>
        }
    })

    fun getPrevious() = endpoint.toAction(Supplier {
        catch {
            getImpl(PagingTraversalType.BACKWARDS) as? PagingObject<T>
        }
    })

    override fun getImpl(type: PagingTraversalType): AbstractPagingObject<T>? {
        return (if (type == PagingTraversalType.FORWARDS) next else previous)?.let { endpoint.get(it) }?.let { json ->
            when {
                itemClazz == SimpleTrack::class.java -> json.toPagingObject<SimpleTrack>(null, endpoint)
                itemClazz == SpotifyCategory::class.java -> json.toPagingObject<SpotifyCategory>(null, endpoint)
                itemClazz == SimpleAlbum::class.java -> json.toPagingObject<SimpleAlbum>(null, endpoint)
                itemClazz == SimplePlaylist::class.java -> json.toPagingObject<SimplePlaylist>(null, endpoint)
                itemClazz == SavedTrack::class.java -> json.toPagingObject<SavedTrack>(null, endpoint)
                itemClazz == SavedAlbum::class.java -> json.toPagingObject<SavedAlbum>(null, endpoint)
                itemClazz == Artist::class.java -> json.toPagingObject<Artist>(null, endpoint)
                itemClazz == Track::class.java -> json.toPagingObject<Track>(null, endpoint)
                itemClazz == PlaylistTrack::class.java -> json.toPagingObject<PlaylistTrack>(null, endpoint)
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

    fun getAll() = endpoint.toAction(Supplier { (getAllImpl() as Sequence<PagingObject<T>>).toList() })
    fun getAllItems() = endpoint.toAction(Supplier { getAll().complete().map { it.items }.flatten() })
}

class CursorBasedPagingObject<T>(
    href: String,
    items: List<T>,
    limit: Int,
    next: String?,
    val cursors: Cursor,
    total: Int
) : AbstractPagingObject<T>(href, items, limit, next, 0, null, total) {
    fun getNext() = endpoint.toAction(Supplier {
        catch {
            getImpl(PagingTraversalType.FORWARDS) as? CursorBasedPagingObject<T>
        }
    })

    fun getAll() = endpoint.toAction(Supplier {
        getAllImpl() as Sequence<CursorBasedPagingObject<T>>
    })

    fun getAllItems() = endpoint.toAction(Supplier {
        getAll().complete().map { it.items }.flatten().toList()
    })

    override fun getImpl(type: PagingTraversalType): AbstractPagingObject<T>? {
        if (type == PagingTraversalType.BACKWARDS) {
            throw IllegalArgumentException("CursorBasedPagingObjects only can go forwards")
        }
        return next?.let {
            val url = endpoint.get(it)
            when {
                itemClazz == PlayHistory::class.java -> url.toCursorBasedPagingObject<PlayHistory>(
                    null,
                    endpoint
                )
                itemClazz == Artist::class.java -> url.toCursorBasedPagingObject<Artist>(
                    null,
                    endpoint
                )
                else -> throw IllegalArgumentException("Unknown type in $href")
            } as? CursorBasedPagingObject<T>
        }
    }

    override fun getAllImpl(): Sequence<AbstractPagingObject<T>> {
        return generateSequence(this) { it.getImpl(PagingTraversalType.FORWARDS) as? CursorBasedPagingObject<T> }
    }
}

abstract class AbstractPagingObject<T>(
    val href: String,
    val items: List<T>,
    val limit: Int,
    val next: String? = null,
    val offset: Int = 0,
    val previous: String? = null,
    val total: Int
) : ArrayList<T>(items) {
    @Json(ignored = true)
    lateinit var endpoint: SpotifyEndpoint

    @Json(ignored = true)
    lateinit var itemClazz: Class<T>

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
