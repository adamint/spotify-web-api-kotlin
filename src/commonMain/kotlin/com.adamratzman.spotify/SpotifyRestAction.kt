/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.models.AbstractPagingObject
import com.adamratzman.spotify.utils.TimeUnit
import com.adamratzman.spotify.utils.getCurrentTimeMs
import com.adamratzman.spotify.utils.schedule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Provides a uniform interface to retrieve, whether synchronously or asynchronously, [T] from Spotify
 */
open class SpotifyRestAction<T> internal constructor(protected val api: SpotifyAPI, val supplier: () -> T) {
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
        return try {
            supplier().also { hasCompletedBacking = true }
        } catch (e: Throwable) {
            throw e
        }
    }

    /**
     * Invoke [supplier] asynchronously with no consumer
     */
    fun queue(): SpotifyRestAction<T> = queue({}, { throw it })

    /**
     * Invoke [supplier] asynchronously and consume [consumer] with the [T] value returned
     *
     * @param consumer to be invoked with [T] after successful completion of [supplier]
     */
    fun queue(consumer: (T) -> Unit): SpotifyRestAction<T> = queue(consumer, {})

    /**
     * Invoke [supplier] asynchronously and consume [consumer] with the [T] value returned
     *
     * @param failure Consumer to invoke when an exception is thrown by [supplier]
     * @param consumer to be invoked with [T] after successful completion of [supplier]
     */
    fun queue(consumer: ((T) -> Unit), failure: ((Throwable) -> Unit)): SpotifyRestAction<T> {
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
            GlobalScope.schedule((runAt - getCurrentTimeMs()).toInt(), TimeUnit.MILLISECONDS) { consumer(result)}
        }
        return this
    }

    override fun toString() = complete().toString()
}

class SpotifyRestActionPaging<Z : Any, T : AbstractPagingObject<Z>>(api: SpotifyAPI, supplier: () -> T) :
    SpotifyRestAction<T>(api, supplier) {

    /**
     * Synchronously retrieve all [AbstractPagingObject] associated with this rest action
     */
    fun getAll() = api.tracks.toAction({ complete().getAllImpl() })

    /**
     * Synchronously retrieve all [Z] associated with this rest action
     */
    fun getAllItems() = api.tracks.toAction({ complete().getAllImpl().toList().map { it.items }.flatten() })

    /**
     * Consume each [Z] by [consumer] as it is retrieved
     */
    fun streamAllItems(consumer: (Z) -> Unit): SpotifyRestAction<Unit> {
        return api.tracks.toAction({
            complete().getAllImpl().toList().forEach { it.items.forEach { item -> consumer(item) } }
        })
    }
}
