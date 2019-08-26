/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
