/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.annotations.SpotifyExperimentalHttpApi
import com.adamratzman.spotify.models.PagingTraversalType.BACKWARDS
import com.adamratzman.spotify.models.PagingTraversalType.FORWARDS
import com.adamratzman.spotify.models.serialization.instantiateAllNeedsApiObjects
import com.adamratzman.spotify.models.serialization.instantiateLateinitsForPagingObject
import com.adamratzman.spotify.models.serialization.toCursorBasedPagingObject
import com.adamratzman.spotify.models.serialization.toNonNullablePagingObject
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
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

public enum class PagingTraversalType {
    BACKWARDS,
    FORWARDS;
}

/**
 * The offset-based nullable paging object is a container for a set of objects. It contains a key called items
 * (whose value is an array of the requested objects) along with other keys like previous, next and
 * limit that can be useful in future calls. Its items are not guaranteed to be not null
 */
@Serializable
public class NullablePagingObject<T : Any>(
    override val href: String,
    override val items: List<T?>,
    override val limit: Int,
    override val next: String? = null,
    override val offset: Int,
    override val previous: String? = null,
    override val total: Int = 0
) : AbstractPagingObject<T, NullablePagingObject<T>>() {
    public fun toPagingObject(): PagingObject<T> {
        val pagingObject = PagingObject(
            href, items.filterNotNull(), limit, next, offset, previous, total
        )
        pagingObject.instantiateLateinitsForPagingObject(itemClazz, api)

        return pagingObject
    }

    override fun iterator(): Iterator<T?> = items.iterator()
    override fun listIterator(): ListIterator<T?> = items.listIterator()
    override fun listIterator(index: Int): ListIterator<T?> = items.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): List<T?> = items.subList(fromIndex, toIndex)
}

/**
 * The offset-based non-nullable paging object is a container for a set of objects. It contains a key called items
 * (whose value is an array of the requested objects) along with other keys like previous, next and
 * limit that can be useful in future calls.
 */
@Serializable
public data class PagingObject<T : Any>(
    override val href: String,
    override val items: List<T>,
    override val limit: Int,
    override val next: String? = null,
    override val offset: Int,
    override val previous: String? = null,
    override val total: Int = 0
) : AbstractPagingObject<T, PagingObject<T>>() {
    override fun get(index: Int): T = super.get(index)!!

    override fun iterator(): Iterator<T> = items.iterator()
    override fun listIterator(): ListIterator<T> = items.listIterator()
    override fun listIterator(index: Int): ListIterator<T> = items.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): List<T> = items.subList(fromIndex, toIndex)

    override suspend fun take(n: Int): List<T> {
        return super.take(n).filterNotNull()
    }
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
public abstract class AbstractPagingObject<T : Any, Z : AbstractPagingObject<T, Z>> : PagingObjectBase<T, Z>(),
    List<T?> {
    @Suppress("UNCHECKED_CAST")
    override suspend fun get(type: PagingTraversalType): Z? {
        return (if (type == FORWARDS) next else previous)?.let { api.defaultEndpoint.get(it) }?.let { json ->
            when (itemClazz) {
                SimpleTrack::class -> json.toNonNullablePagingObject(
                    SimpleTrack.serializer(),
                    null,
                    api,
                    api.spotifyApiOptions.json,
                    true
                )
                SpotifyCategory::class -> json.toNonNullablePagingObject(
                    SpotifyCategory.serializer(),
                    null,
                    api,
                    api.spotifyApiOptions.json,
                    true
                )
                SimpleAlbum::class -> json.toNonNullablePagingObject(
                    SimpleAlbum.serializer(),
                    null,
                    api,
                    api.spotifyApiOptions.json,
                    true
                )
                SimplePlaylist::class -> json.toNonNullablePagingObject(
                    SimplePlaylist.serializer(),
                    null,
                    api,
                    api.spotifyApiOptions.json,
                    true
                )
                SavedTrack::class -> json.toNonNullablePagingObject(
                    SavedTrack.serializer(),
                    null,
                    api,
                    api.spotifyApiOptions.json,
                    true
                )
                SavedAlbum::class -> json.toNonNullablePagingObject(
                    SavedAlbum.serializer(),
                    null,
                    api,
                    api.spotifyApiOptions.json,
                    true
                )
                Artist::class -> json.toNonNullablePagingObject(
                    Artist.serializer(),
                    null,
                    api,
                    api.spotifyApiOptions.json,
                    true
                )
                Track::class -> json.toNonNullablePagingObject(
                    Track.serializer(),
                    null,
                    api,
                    api.spotifyApiOptions.json,
                    true
                )
                PlaylistTrack::class -> json.toNonNullablePagingObject(
                    PlaylistTrack.serializer(),
                    null,
                    api,
                    api.spotifyApiOptions.json,
                    true
                )
                else -> throw IllegalArgumentException("Unknown type in $href response")
            } as? Z
        }
    }

    override suspend fun getWithNextTotalPagingObjects(total: Int): List<Z> {
        @Suppress("UNCHECKED_CAST") val pagingObjects = mutableListOf(this as Z)

        var nxt = next?.let { getNext() }
        while (pagingObjects.size < total && nxt != null) {
            pagingObjects.add(nxt)
            nxt = nxt.next?.let { nxt?.getNext() }
        }

        return pagingObjects.distinctBy { it.href }
    }

    override suspend fun getAllPagingObjects(): List<Z> {
        val pagingObjects = mutableListOf<Z>()
        var prev = previous?.let { getPrevious() }
        while (prev != null) {

            pagingObjects.add(prev)
            prev = prev.previous?.let { prev?.getPrevious() }
        }
        pagingObjects.reverse() // closer we are to current, the further we are from the start

        @Suppress("UNCHECKED_CAST")
        pagingObjects.add(this as Z)
        var nxt = next?.let { getNext() }
        while (nxt != null) {
            pagingObjects.add(nxt)
            nxt = nxt.next?.let { nxt?.getNext() }
        }

        // we don't need to reverse here, as it's in order
        return pagingObjects
    }

    /**
     * Synchronously retrieve the next [total] paging objects associated with this [AbstractPagingObject], including this [AbstractPagingObject].
     *
     * @param total The total amount of [AbstractPagingObject] to request, which includes this [AbstractPagingObject].
     * @since 3.0.0
     */
    @Suppress("UNCHECKED_CAST")
    public suspend fun getWithNext(total: Int): List<Z> = getWithNextTotalPagingObjects(total)

    /**
     * Get all items of type [T] associated with the request
     */
    public override suspend fun getAllItems(): List<T?> = getAllPagingObjects().map { it.items }.flatten()
}

/**
 * The cursor-based paging object is a container for a set of objects. It contains a key called
 * items (whose value is an array of the requested objects) along with other keys like next and
 * cursors that can be useful in future calls.
 *
 * @param href A link to the Web API endpoint returning the full result of the request.
 * @param items The requested data.
 * @param limit The maximum number of items in the response (as set in the query or by default).
 * @param next URL to the next page of items. ( null if none)
 * @param total The maximum number of items available to return.
 * @param cursor The cursors used to find the next set of items. If [items] is empty, cursor may be null.
 */
@Serializable
public data class CursorBasedPagingObject<T : Any>(
    override val href: String,
    override val items: List<T>,
    override val limit: Int,
    override val next: String? = null,
    @SerialName("cursors") public val cursor: Cursor? = null,
    override val total: Int = 0,
    override val offset: Int = 0,
    override val previous: String? = null
) : PagingObjectBase<T, CursorBasedPagingObject<T>>() {
    /**
     * Synchronously retrieve the next [total] paging objects associated with this [CursorBasedPagingObject], including this [CursorBasedPagingObject].
     *
     * @param total The total amount of [CursorBasedPagingObject] to request, which includes this [CursorBasedPagingObject].
     * @since 3.0.0
     */
    @Suppress("UNCHECKED_CAST")
    public suspend fun getWithNext(total: Int): List<CursorBasedPagingObject<T>> = getWithNextTotalPagingObjects(total)

    /**
     * Get all items of type [T] associated with the request
     */
    override suspend fun getAllItems(): List<T?> = getAllPagingObjects().map { it.items }.flatten()

    override suspend fun get(type: PagingTraversalType): CursorBasedPagingObject<T>? {
        require(type != BACKWARDS) { "CursorBasedPagingObjects only can go forwards" }
        return next?.let { getCursorBasedPagingObject(it) }
    }

    @Suppress("UNCHECKED_CAST")
    public suspend fun getCursorBasedPagingObject(url: String): CursorBasedPagingObject<T>? {
        val json = api.defaultEndpoint.get(url)
        return when (itemClazz) {
            PlayHistory::class -> json.toCursorBasedPagingObject(
                PlayHistory::class,
                PlayHistory.serializer(),
                null,
                api,
                api.spotifyApiOptions.json
            )
            Artist::class -> json.toCursorBasedPagingObject(
                Artist::class,
                Artist.serializer(),
                null,
                api,
                api.spotifyApiOptions.json
            )
            else -> throw IllegalArgumentException("Unknown type in $href")
        } as? CursorBasedPagingObject<T>
    }

    override suspend fun getAllPagingObjects(): List<CursorBasedPagingObject<T>> {
        val pagingObjects = mutableListOf<CursorBasedPagingObject<T>>()
        var currentPagingObject = this@CursorBasedPagingObject
        pagingObjects.add(currentPagingObject)
        while (true) {
            currentPagingObject = currentPagingObject.get(FORWARDS) ?: break
            pagingObjects.add(currentPagingObject)
        }
        return pagingObjects
    }

    override suspend fun getWithNextTotalPagingObjects(total: Int): List<CursorBasedPagingObject<T>> {
        val pagingObjects = mutableListOf(this)

        var nxt = getNext()
        while (pagingObjects.size < total && nxt != null) {
            pagingObjects.add(nxt)
            nxt = nxt.next?.let { nxt?.getNext() }
        }

        return pagingObjects.distinctBy { it.href }
    }

    override fun get(index: Int): T = super.get(index)!!
    override fun iterator(): Iterator<T> = items.iterator()
    override fun listIterator(): ListIterator<T> = items.listIterator()
    override fun listIterator(index: Int): ListIterator<T> = items.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): List<T> = items.subList(fromIndex, toIndex)

    override suspend fun take(n: Int): List<T> {
        return super.take(n).filterNotNull()
    }
}

/**
 * The cursor to use as key to find the next (or previous) page of items.
 *
 * @param before The cursor to use as key to find the previous page of items.
 * @param after The cursor to use as key to find the next page of items.
 */
@Serializable
public data class Cursor(val before: String? = null, val after: String? = null)

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
public abstract class PagingObjectBase<T : Any, Z : PagingObjectBase<T, Z>> : List<T?>, NeedsApi() {
    public abstract val href: String
    public abstract val items: List<T?>
    public abstract val limit: Int
    public abstract val next: String?
    public abstract val offset: Int
    public abstract val previous: String?
    public abstract val total: Int

    @Suppress("UNCHECKED_CAST")
    override fun getMembersThatNeedApiInstantiation(): List<NeedsApi?> {
        return if (items.getOrNull(0) !is NeedsApi) listOf(this)
        else (items as List<NeedsApi>) + listOf(this)
    }

    @Transient
    internal var itemClazz: KClass<T>? = null

    internal abstract suspend fun get(type: PagingTraversalType): Z?

    /**
     * Retrieve all [PagingObjectBase] associated with this rest action
     */
    public abstract suspend fun getAllPagingObjects(): List<Z>

    /**
     * Retrieve all [PagingObjectBase] associated with this rest action
     */
    public fun getAllPagingObjectsRestAction(): SpotifyRestAction<List<Z>> = SpotifyRestAction { getAllPagingObjects() }


    /**
     * Retrieve all [T] associated with this rest action
     */
    public abstract suspend fun getAllItems(): List<T?>

    /**
     * Retrieve all [T] associated with this rest action
     */
    public fun getAllItemsRestAction(): SpotifyRestAction<List<T?>> = SpotifyRestAction { getAllItems() }

    /**
     * Synchronously retrieve the next [total] paging objects associated with this [PagingObjectBase], including this [PagingObjectBase].
     *
     * @param total The total amount of [PagingObjectBase] to request, which includes this [PagingObjectBase].
     * @since 3.0.0
     */
    public abstract suspend fun getWithNextTotalPagingObjects(total: Int): List<Z>

    /**
     * Synchronously retrieve the next [total] paging objects associated with this [PagingObjectBase], including this [PagingObjectBase].
     *
     * @param total The total amount of [PagingObjectBase] to request, which includes this [PagingObjectBase].
     * @since 3.0.0
     */
    public fun getWithNextTotalPagingObjectsRestAction(total: Int): SpotifyRestAction<List<Z>> = SpotifyRestAction { getWithNextTotalPagingObjects(total) }

    public suspend fun getNext(): Z? = get(FORWARDS)

    public fun getNextRestAction(): SpotifyRestAction<Z?> = SpotifyRestAction { getNext() }

    public suspend fun getPrevious(): Z? = get(BACKWARDS)

    public fun getPreviousRestAction(): SpotifyRestAction<Z?> = SpotifyRestAction { getPrevious() }

    /**
     * Get all items of type [T] associated with the request. Filters out null objects.
     */
    public suspend fun getAllItemsNotNull(): List<T> = getAllItems().filterNotNull()

    /**
     * Get all items of type [T] associated with the request. Filters out null objects.
     */
    public fun getAllItemsNotNullRestAction(): SpotifyRestAction<List<T>> = SpotifyRestAction { getAllItemsNotNull() }

    /**
     * Retrieve the items associated with the next [total] paging objects associated with this rest action, including the current one.
     *
     * @param total The total amount of [PagingObjectBase] to request, including the [PagingObjectBase] associated with the current request.
     * @since 3.0.0
     */
    public suspend fun getWithNextItems(total: Int): List<T?> =
        getWithNextTotalPagingObjects(total).map { it.items }.flatten()

    /**
     * Retrieve the items associated with the next [total] paging objects associated with this rest action, including the current one.
     *
     * @param total The total amount of [PagingObjectBase] to request, including the [PagingObjectBase] associated with the current request.
     * @since 3.0.0
     */
    public fun getWithNextItemsRestAction(total: Int): SpotifyRestAction<List<T?>> = SpotifyRestAction { getWithNextItems(total) }
    /**
     * Flow from current page backwards.
     * */
    public fun flowBackward(): Flow<Z> = flow<Z> {
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
    public fun flowForward(): Flow<Z> = flow<Z> {
        if (next == null) return@flow
        var next = getNext()
        while (next != null) {
            emit(next)
            next = next.getNext()
        }
    }.flowOn(Dispatchers.Default)

    @ExperimentalCoroutinesApi
    public fun flowStartOrdered(): Flow<Z> =
        flow {
            if (previous == null) return@flow
            flowBackward().toList().reversed().also {
                emitAll(it.asFlow())
            }
        }.flowOn(Dispatchers.Default)

    @ExperimentalCoroutinesApi
    public fun flowEndOrdered(): Flow<Z> = flowForward()

    /**
     * Flow the paging action ordered. This can be less performant than [flow] if you are in the middle of the pages.
     * */
    @FlowPreview
    @ExperimentalCoroutinesApi
    public fun flowOrdered(context: CoroutineContext = Dispatchers.Default): Flow<T?> = flow {
        emitAll(flowPagingObjectsOrdered().flatMapConcat { it.asFlow() })
    }.flowOn(context)

    /**
     * Flow the paging objects ordered. This can be less performant than [flowPagingObjects] if you are in the middle of the pages.
     * */
    @ExperimentalCoroutinesApi
    public fun flowPagingObjectsOrdered(context: CoroutineContext = Dispatchers.Default): Flow<Z> =
        flow {
            this@PagingObjectBase.also { master ->
                emitAll(master.flowStartOrdered())
                @Suppress("UNCHECKED_CAST")
                emit(master as Z)
                emitAll(master.flowEndOrdered())
            }
        }.flowOn(context)

    /**
     * Flow the Paging action.
     * */
    @FlowPreview
    @ExperimentalCoroutinesApi
    public fun flow(context: CoroutineContext = Dispatchers.Default): Flow<T?> = flow {
        emitAll(flowPagingObjects().flatMapConcat { it.asFlow() })
    }.flowOn(context)

    /**
     * Flow the paging objects.
     * */
    @ExperimentalCoroutinesApi
    public fun flowPagingObjects(context: CoroutineContext = Dispatchers.Default): Flow<Z> =
        flow {
            this@PagingObjectBase.also { master ->
                emitAll(master.flowBackward())
                @Suppress("UNCHECKED_CAST")
                emit(master as Z)
                emitAll(master.flowForward())
            }
        }.flowOn(context)

    override val size: Int get() = items.size
    override fun contains(element: T?): Boolean = items.contains(element)
    override fun containsAll(elements: Collection<T?>): Boolean = items.containsAll(elements)
    override fun indexOf(element: T?): Int = items.indexOf(element)
    override fun isEmpty(): Boolean = items.isEmpty()
    override fun lastIndexOf(element: T?): Int = items.lastIndexOf(element)
    override fun get(index: Int): T? = items[index]

    /**
     * Returns a list containing at most first [n] elements. Note that additional requests may be performed.
     * The [limit] used in the request used to produce this [PagingObjectBase] will be respected, so choose [limit] carefully.
     */
    public open suspend fun take(n: Int): List<T?> {
        if (n < 0) throw IllegalArgumentException("n must be non-negative.")
        if (n in items.indices) return items.take(n)
        return items + (getNext()?.take(n - size) ?: listOf())
    }
}

internal fun Any.instantiateLateinitsIfPagingObjects(api: GenericSpotifyApi) = when (this) {
    is FeaturedPlaylists -> this.playlists
    is Show -> this.episodes
    is Album -> this.tracks
    is Playlist -> this.tracks
    else -> null
}?.let { it.api = api; it.getMembersThatNeedApiInstantiation().instantiateAllNeedsApiObjects(api) }
