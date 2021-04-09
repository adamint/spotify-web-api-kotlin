/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.utils.TimeUnit
import com.adamratzman.spotify.utils.getCurrentTimeMs
import com.adamratzman.spotify.utils.runBlockingOnJvmAndNative
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.jvm.JvmOverloads

/**
 * Provides a uniform interface to retrieve, whether synchronously or asynchronously, [T] from Spotify
 */
public open class SpotifyRestAction<T> internal constructor(public val supplier: suspend () -> T) {
    private var hasRunBacking: Boolean = false
    private var hasCompletedBacking: Boolean = false

    /**
     * Whether this REST action has been *commenced*.
     *
     * Not to be confused with [hasCompleted]
     */
    public fun hasRun(): Boolean = hasRunBacking

    /**
     * Whether this REST action has been fully *completed*
     */
    public fun hasCompleted(): Boolean = hasCompletedBacking

    /**
     * Invoke [supplier] and synchronously retrieve [T]. This is only available on JVM/Native and will fail on JS.
     */
    public fun complete(): T = runBlockingOnJvmAndNative {
        suspendComplete()
    }

    /**
     * Suspend the coroutine, invoke [SpotifyRestAction.supplier] asynchronously/queued and resume with result [T]
     * */
    public suspend fun suspendQueue(): T = suspendCoroutine { continuation ->
        queue({ throwable ->
            continuation.resumeWithException(throwable)
        }) { result ->
            continuation.resume(result)
        }
    }

    /**
     * Switch to given [context][context], invoke [SpotifyRestAction.supplier] and synchronously retrieve [T]
     *
     * @param context The context to execute the [SpotifyRestAction.complete] in
     * */
    @Suppress("UNCHECKED_CAST")
    @JvmOverloads
    public suspend fun suspendComplete(context: CoroutineContext = Dispatchers.Default): T = withContext(context) {
        hasRunBacking = true
        return@withContext try {
            supplier().also { hasCompletedBacking = true }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            throw e
        }
    }

    /**
     * Invoke [supplier] asynchronously and consume [consumer] with the [T] value returned
     *
     * @param failure Consumer to invoke when an exception is thrown by [supplier]
     * @param consumer to be invoked with [T] after successful completion of [supplier]
     */
    @JvmOverloads
    public fun queue(failure: ((Throwable) -> Unit) = { throw it }, consumer: ((T) -> Unit) = {}) {
        hasRunBacking = true
        GlobalScope.launch {
            try {
                val result = suspendComplete()
                consumer(result)
            } catch (e: CancellationException) {
                throw e
            } catch (t: Throwable) {
                failure(t)
            }
        }
    }

    /**
     * Invoke [supplier] asynchronously immediately and invoke [consumer] after the specified quantity of time.
     *
     * @param quantity amount of time
     * @param timeUnit the unit that [quantity] is in
     * @param consumer to be invoked with [T] after successful completion of [supplier]
     */
    @JvmOverloads
    public fun queueAfter(
        quantity: Int,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        scope: CoroutineScope = GlobalScope,
        failure: (Throwable) -> Unit = { throw it },
        consumer: (T) -> Unit
    ) {
        val runAt = getCurrentTimeMs() + timeUnit.toMillis(quantity.toLong())
        GlobalScope.launch {
            delay(getCurrentTimeMs() - runAt)

            try {
                consumer(suspendComplete())
            } catch (e: CancellationException) {
                throw e
            } catch (t: Throwable) {
                failure(t)
            }
        }
    }

    override fun toString(): String = complete().toString()
}