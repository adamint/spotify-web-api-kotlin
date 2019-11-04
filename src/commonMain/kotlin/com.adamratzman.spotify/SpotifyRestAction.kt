/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.models.AbstractPagingObject
import com.adamratzman.spotify.utils.TimeUnit
import com.adamratzman.spotify.utils.getCurrentTimeMs
import com.adamratzman.spotify.utils.schedule
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Provides a uniform interface to retrieve, whether synchronously or asynchronously, [T] from Spotify
 */
open class SpotifyRestAction<T> internal constructor(protected val api: SpotifyApi, val supplier: () -> T) {
    private var hasRunBacking: Boolean = false
    private var hasCompletedBacking: Boolean = false

    /**
     * Whether this REST action has been *commenced*.
     *
     * Not to be confused with [hasCompleted]
     */
    fun hasRun() = hasRunBacking

    /**
     * Whether this REST action has been fully *completed*
     */
    fun hasCompleted() = hasCompletedBacking

    /**
     * Invoke [supplier] and synchronously retrieve [T]
     */
    fun complete(): T {
        hasRunBacking = true
        return supplier().also { hasCompletedBacking = true }
    }

    /**
     * Suspend the coroutine, invoke [SpotifyRestAction.supplier] asynchronously/queued and resume with result [T]
     * */
    suspend fun <T> SpotifyRestAction<T>.suspendQueue(): T {
        return suspendCoroutine { continuation ->
            queue({ throwable ->
                continuation.resumeWithException(throwable)
            }) { result ->
                continuation.resume(result)
            }
        }
    }

    /**
     * Switch to given [context][dispatcher], invoke [SpotifyRestAction.supplier] and synchronously retrieve [T]
     *
     * @param dispatcher The context to execute the [SpotifyRestAction.complete] in
     * */
    @Suppress("UNCHECKED_CAST")
    suspend fun <T> suspendComplete(dispatcher: CoroutineDispatcher = Dispatchers.Default): T =
        withContext(dispatcher) {
            complete() as T
        }

    /**
     * Invoke [supplier] asynchronously with no consumer
     */
    fun queue(): SpotifyRestAction<T> = queue({ throw it }, {})

    /**
     * Invoke [supplier] asynchronously and consume [consumer] with the [T] value returned
     *
     * @param consumer to be invoked with [T] after successful completion of [supplier]
     */
    fun queue(consumer: (T) -> Unit): SpotifyRestAction<T> = queue({ throw it }, consumer)

    /**
     * Invoke [supplier] asynchronously and consume [consumer] with the [T] value returned
     *
     * @param failure Consumer to invoke when an exception is thrown by [supplier]
     * @param consumer to be invoked with [T] after successful completion of [supplier]
     */
    fun queue(failure: ((Throwable) -> Unit), consumer: ((T) -> Unit)): SpotifyRestAction<T> {
        hasRunBacking = true
        GlobalScope.launch {
            try {
                val result = complete()
                consumer(result)
            } catch (t: Throwable) {
                failure(t)
            }
        }

        return this
    }

    /**
     * Invoke [supplier] asynchronously immediately and invoke [consumer] after the specified quantity of time
     *
     * @param quantity amount of time
     * @param timeUnit the unit that [quantity] is in
     * @param consumer to be invoked with [T] after successful completion of [supplier]
     */
    fun queueAfter(quantity: Int, timeUnit: TimeUnit = TimeUnit.SECONDS, consumer: (T) -> Unit): SpotifyRestAction<T> {
        val runAt = getCurrentTimeMs() + timeUnit.toMillis(quantity.toLong())
        queue { result ->
            GlobalScope.schedule((runAt - getCurrentTimeMs()).toInt(), TimeUnit.MILLISECONDS) { consumer(result) }
        }
        return this
    }

    override fun toString() = complete().toString()
}

class SpotifyRestActionPaging<Z : Any, T : AbstractPagingObject<Z>>(api: SpotifyApi, supplier: () -> T) :
    SpotifyRestAction<T>(api, supplier) {

    /**
     * Synchronously retrieve all [AbstractPagingObject] associated with this rest action
     */
    fun getAll() = api.tracks.toAction { complete().getAllImpl() }

    /**
     * Synchronously retrieve all [Z] associated with this rest action
     */
    fun getAllItems() = api.tracks.toAction { complete().getAllImpl().toList().map { it.items }.flatten() }

    /**
     * Consume each [Z] by [consumer] as it is retrieved
     */
    fun streamAllItems(consumer: (Z) -> Unit): SpotifyRestAction<Unit> {
        return api.tracks.toAction {
            complete().getAllImpl().toList().forEach { it.items.forEach { item -> consumer(item) } }
        }
    }
}

/**
 * Flow the paging action ordered. This can be less performant than [flow] if you are in the middle of the pages.
 * */
@Suppress("UNCHECKED_CAST")
@FlowPreview
@ExperimentalCoroutinesApi
fun <Z : Any, T : AbstractPagingObject<Z>> SpotifyRestActionPaging<Z, T>.flowOrdered(): Flow<T> = flow<T> {
    emitAll(flowPagingObjectsOrdered().flatMapConcat { it.asFlow() as Flow<T> })
}.flowOn(Dispatchers.Default)

/**
 * Flow the paging objects ordered. This can be less performant than [flowPagingObjects] if you are in the middle of the pages.
 * */
@ExperimentalCoroutinesApi
fun <Z : Any, T : AbstractPagingObject<Z>> SpotifyRestActionPaging<Z, T>.flowPagingObjectsOrdered(): Flow<AbstractPagingObject<Z>> =
    flow<AbstractPagingObject<Z>> {
        complete().also { master ->
            emitAll(master.flowStartOrdered())
            emit(master)
            emitAll(master.flowEndOrdered())
        }
    }.flowOn(Dispatchers.Default)

/**
 * Flow the Paging action.
 * */
@FlowPreview
@ExperimentalCoroutinesApi
fun <Z : Any, T : AbstractPagingObject<Z>> SpotifyRestActionPaging<Z, T>.flow(): Flow<Z> = flow<Z> {
    emitAll(flowPagingObjects().flatMapConcat { it.asFlow() })
}.flowOn(Dispatchers.Default)

/**
 * Flow the paging objects.
 * */
@ExperimentalCoroutinesApi
fun <Z : Any, T : AbstractPagingObject<Z>> SpotifyRestActionPaging<Z, T>.flowPagingObjects(): Flow<AbstractPagingObject<Z>> =
    flow<AbstractPagingObject<Z>> {
        complete().also { master ->
            emitAll(master.flowBackward())
            emit(master)
            emitAll(master.flowForward())
        }
    }.flowOn(Dispatchers.Default)
