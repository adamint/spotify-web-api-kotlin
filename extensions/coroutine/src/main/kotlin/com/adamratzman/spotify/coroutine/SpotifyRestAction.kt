/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.coroutine

import com.adamratzman.spotify.SpotifyRestAction
import com.adamratzman.spotify.SpotifyRestActionPaging
import com.adamratzman.spotify.models.AbstractPagingObject
import com.adamratzman.spotify.models.getNext
import com.adamratzman.spotify.models.getPrevious
import kotlinx.coroutines.CoroutineDispatcher
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
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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
@JvmOverloads
suspend fun <T> SpotifyRestAction<T>.suspendComplete(dispatcher: CoroutineDispatcher = Dispatchers.IO): T =
    withContext(dispatcher) {
        complete()
    }

/**
 * Flow the Paging action.
 * */
@FlowPreview
@ExperimentalCoroutinesApi
fun <Z, T : AbstractPagingObject<Z>> SpotifyRestActionPaging<Z, T>.flow(): Flow<Z> = flow<Z> {
    emitAll(flowPagingObjects().flatMapConcat { it.asFlow() })
}.flowOn(Dispatchers.IO)

/**
 * Flow the paging objects.
 * */
@ExperimentalCoroutinesApi
fun <Z, T : AbstractPagingObject<Z>> SpotifyRestActionPaging<Z, T>.flowPagingObjects(): Flow<AbstractPagingObject<Z>> =
    flow<AbstractPagingObject<Z>> {
        complete().also { master ->
            emitAll(master.flowBackward())
            emit(master)
            emitAll(master.flowForward())
        }
    }.flowOn(Dispatchers.IO)

/**
 * Flow from current page backwards.
 * */
@ExperimentalCoroutinesApi
fun <Z> AbstractPagingObject<Z>.flowBackward(): Flow<AbstractPagingObject<Z>> = flow<AbstractPagingObject<Z>> {
    if (previous == null) return@flow
    var next = getPrevious()
    while (next != null) {
        emit(next)
        next = next.getPrevious()
    }
}.flowOn(Dispatchers.IO)

/**
 * Flow from current page forwards.
 * */
@ExperimentalCoroutinesApi
fun <Z> AbstractPagingObject<Z>.flowForward(): Flow<AbstractPagingObject<Z>> = flow<AbstractPagingObject<Z>> {
    if (next == null) return@flow
    var next = getNext()
    while (next != null) {
        emit(next)
        next = next.getNext()
    }
}.flowOn(Dispatchers.IO)

/**
 * Flow the paging action ordered. This can be less performant than [flow] if you are in the middle of the pages.
 * */
@FlowPreview
@ExperimentalCoroutinesApi
fun <Z, T : AbstractPagingObject<Z>> SpotifyRestActionPaging<Z, T>.flowOrdered(): Flow<Z> = flow<Z> {
    emitAll(flowPagingObjectsOrdered().flatMapConcat { it.asFlow() })
}.flowOn(Dispatchers.IO)

/**
 * Flow the paging objects ordered. This can be less performant than [flowPagingObjects] if you are in the middle of the pages.
 * */
@ExperimentalCoroutinesApi
fun <Z, T : AbstractPagingObject<Z>> SpotifyRestActionPaging<Z, T>.flowPagingObjectsOrdered(): Flow<AbstractPagingObject<Z>> =
    flow<AbstractPagingObject<Z>> {
        complete().also { master ->
            emitAll(master.flowStartOrdered())
            emit(master)
            emitAll(master.flowEndOrdered())
        }
    }.flowOn(Dispatchers.IO)

@ExperimentalCoroutinesApi
private fun <Z> AbstractPagingObject<Z>.flowStartOrdered(): Flow<AbstractPagingObject<Z>> =
    flow<AbstractPagingObject<Z>> {
        if (previous == null) return@flow
        flowBackward().toList().reversed().also {
            emitAll(it.asFlow())
        }
    }.flowOn(Dispatchers.IO)

@ExperimentalCoroutinesApi
private fun <Z> AbstractPagingObject<Z>.flowEndOrdered(): Flow<AbstractPagingObject<Z>> = flowForward()
