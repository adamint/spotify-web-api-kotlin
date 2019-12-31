/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.http.SpotifyEndpoint
import com.adamratzman.spotify.models.serialization.toCursorBasedPagingObject
import com.adamratzman.spotify.models.serialization.toPagingObject
import com.adamratzman.spotify.utils.runBlocking
import kotlin.reflect.KClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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
    override val href: String,
    override val items: List<T>,
    override val limit: Int,
    override val next: String? = null,
    override val offset: Int,
    override val previous: String? = null,
    override val total: Int = 0
) : AbstractPagingObject<T>(href, items, limit, next, offset, previous, total) {
    @Suppress("UNCHECKED_CAST")
    override suspend fun getImpl(type: PagingTraversalType): AbstractPagingObject<T>? {
        val endpointFinal = endpoint!!
        return (if (type == PagingTraversalType.FORWARDS) next else previous)?.let { endpoint!!.get(it) }?.let { json ->
            when (itemClazz) {
                SimpleTrack::class -> json.toPagingObject(SimpleTrack.serializer(), null, endpointFinal, endpointFinal.api.json)
                SpotifyCategory::class -> json.toPagingObject(SpotifyCategory.serializer(), null, endpointFinal, endpointFinal.api.json)
                SimpleAlbum::class -> json.toPagingObject(SimpleAlbum.serializer(), null, endpointFinal, endpointFinal.api.json)
                SimplePlaylist::class -> json.toPagingObject(SimplePlaylist.serializer(), null, endpointFinal, endpointFinal.api.json)
                SavedTrack::class -> json.toPagingObject(SavedTrack.serializer(), null, endpointFinal, endpointFinal.api.json)
                SavedAlbum::class -> json.toPagingObject(SavedAlbum.serializer(), null, endpointFinal, endpointFinal.api.json)
                Artist::class -> json.toPagingObject(Artist.serializer(), null, endpointFinal, endpointFinal.api.json)
                Track::class -> json.toPagingObject(Track.serializer(), null, endpointFinal, endpointFinal.api.json)
                PlaylistTrack::class -> json.toPagingObject(PlaylistTrack.serializer(), null, endpointFinal, endpointFinal.api.json)
                else -> throw IllegalArgumentException("Unknown type in $href response")
            } as? PagingObject<T>
        }
    }

    override suspend fun getAllImpl(): Sequence<AbstractPagingObject<T>> {
        val pagingObjects = mutableListOf<AbstractPagingObject<T>>()
        var prev = previous?.let { getPrevious() }
        while (prev != null) {
            pagingObjects.add(prev)
            prev = prev.previous?.let { prev?.getPrevious() }
        }
        pagingObjects.reverse() // closer we are to current, the further we are from the start

        pagingObjects.add(this)

        var nxt = next?.let { getNext() }
        while (nxt != null) {
            pagingObjects.add(nxt)
            nxt = nxt.next?.let { nxt?.getNext() }
        }
        // we don't need to reverse here, as it's in order
        return pagingObjects.asSequence()
    }

    /**
     * Get all PagingObjects associated with the request
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun getAll() = endpoint!!.toAction { (getAllImpl() as Sequence<PagingObject<T>>).toList() }

    /**
     * Get all items of type [T] associated with the request
     */
    suspend fun getAllItems() = endpoint!!.toAction { getAll().complete().map { it.items }.flatten() }
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
    override val href: String,
    override val items: List<T>,
    override val limit: Int,
    override val next: String? = null,
    @SerialName("cursors") val cursor: Cursor,
    override val total: Int = 0
) : AbstractPagingObject<T>(href, items, limit, next, 0, null, total) {
    /**
     * Get all CursorBasedPagingObjects associated with the request
     */
    @Suppress("UNCHECKED_CAST")
    fun getAll() = endpoint!!.toAction {
        getAllImpl() as Sequence<CursorBasedPagingObject<T>>
    }

    /**
     * Get all items of type [T] associated with the request
     */
    fun getAllItems() = endpoint!!.toAction {
        getAll().complete().map { it.items }.flatten().toList()
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun getImpl(type: PagingTraversalType): AbstractPagingObject<T>? {
        require(type != PagingTraversalType.BACKWARDS) { "CursorBasedPagingObjects only can go forwards" }
        return next?.let {
            val url = endpoint!!.get(it)
            when (itemClazz) {
                PlayHistory::class -> url.toCursorBasedPagingObject(
                    PlayHistory.serializer(),
                    null,
                    endpoint!!,
                    endpoint!!.api.json
                )
                Artist::class -> url.toCursorBasedPagingObject(
                    Artist.serializer(),
                    null,
                    endpoint!!,
                    endpoint!!.api.json
                )
                else -> throw IllegalArgumentException("Unknown type in $href")
            } as? CursorBasedPagingObject<T>
        }
    }

    override suspend fun getAllImpl(): Sequence<AbstractPagingObject<T>> {
        return generateSequence(this) { runBlocking { it.getImpl(PagingTraversalType.FORWARDS) as? CursorBasedPagingObject<T> } }
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
    @Transient open val href: String = TRANSIENT_EMPTY_STRING,
    @Transient open val items: List<T> = listOf(),
    @Transient open val limit: Int = -1,
    @Transient open val next: String? = null,
    @Transient open val offset: Int = 0,
    @Transient open val previous: String? = null,
    @Transient open val total: Int = -1
) : List<T> by items {
    @Transient
    internal var endpoint: SpotifyEndpoint? = null

    @Transient
    internal var itemClazz: KClass<T>? = null

    internal abstract suspend fun getImpl(type: PagingTraversalType): AbstractPagingObject<T>?
    internal abstract suspend fun getAllImpl(): Sequence<AbstractPagingObject<T>>

    private suspend fun getNextImpl() = getImpl(PagingTraversalType.FORWARDS)
    private suspend fun getPreviousImpl() = getImpl(PagingTraversalType.BACKWARDS)

    suspend fun getNext(): AbstractPagingObject<T>? = getNextImpl()
    suspend fun getPrevious(): AbstractPagingObject<T>? = getPreviousImpl()

    /**
     * Flow from current page backwards.
     * */
    @ExperimentalCoroutinesApi
    fun flowBackward(): Flow<AbstractPagingObject<T>> = flow<AbstractPagingObject<T>> {
        if (previous == null) return@flow
        var next = getPrevious()
        while (next != null) {
            emit(next)
            next = next.getPrevious()
        }
    }.flowOn(Dispatchers.Default)

    /**
     * Flow from current page forwards.
     * */
    @ExperimentalCoroutinesApi
    fun flowForward(): Flow<AbstractPagingObject<T>> = flow<AbstractPagingObject<T>> {
        if (next == null) return@flow
        var next = getNext()
        while (next != null) {
            emit(next)
            next = next.getNext()
        }
    }.flowOn(Dispatchers.Default)

    @ExperimentalCoroutinesApi
    fun flowStartOrdered(): Flow<AbstractPagingObject<T>> =
        flow<AbstractPagingObject<T>> {
            if (previous == null) return@flow
            flowBackward().toList().reversed().also {
                emitAll(it.asFlow())
            }
        }.flowOn(Dispatchers.Default)

    @ExperimentalCoroutinesApi
    fun flowEndOrdered(): Flow<AbstractPagingObject<T>> = flowForward()
}

internal fun Any.instantiatePagingObjects(spotifyApi: SpotifyApi<*, *>) = when (this) {
    is FeaturedPlaylists -> this.playlists
    is Album -> this.tracks
    is Playlist -> this.tracks
    else -> null
}.let { it?.endpoint = spotifyApi.tracks; this }
