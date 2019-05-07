/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.models.AbstractPagingObject
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

/**
 * Provides a uniform interface to retrieve, whether synchronously or asynchronously, [T] from Spotify
 */
open class SpotifyRestAction<T> internal constructor(protected val api: SpotifyAPI, private val supplier: Supplier<T>) {

    /**
     * Invoke [supplier] and synchronously retrieve [T]
     */
    fun complete(): T {
        return try {
            supplier.get()
        } catch (e: Throwable) {
            throw e
        }
    }

    /**
     * Invoke [supplier] asynchronously with no consumer
     */
    fun queue() = queue({}, { throw it })

    /**
     * Invoke [supplier] asynchronously and consume [consumer] with the [T] value returned
     *
     * @param consumer to be invoked with [T] after successful completion of [supplier]
     */
    fun queue(consumer: (T) -> Unit) = queue(consumer, {})

    /**
     * Invoke [supplier] asynchronously and consume [consumer] with the [T] value returned
     *
     * @param failure Consumer to invoke when an exception is thrown by [supplier]
     * @param consumer to be invoked with [T] after successful completion of [supplier]
     */
    fun queue(consumer: ((T) -> Unit), failure: ((Throwable) -> Unit)) {
        api.executor.execute {
            try {
                val result = complete()
                consumer(result)
            } catch (t: Throwable) {
                failure(t)
            }
        }
    }

    /**
     * Return [supplier] as a [CompletableFuture]
     */
    fun asFuture() = CompletableFuture.supplyAsync(supplier)

    /**
     * Invoke [supplier] asynchronously immediately and invoke [consumer] after the specified quantity of time
     *
     * @param quantity amount of time
     * @param timeUnit the unit that [quantity] is in
     * @param consumer to be invoked with [T] after successful completion of [supplier]
     */
    fun queueAfter(quantity: Int, timeUnit: TimeUnit = TimeUnit.SECONDS, consumer: (T) -> Unit) {
        val runAt = System.currentTimeMillis() + timeUnit.toMillis(quantity.toLong())
        queue { result ->
            api.executor.schedule({ consumer(result) }, runAt - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        }
    }

    override fun toString() = complete().toString()
}

class SpotifyRestActionPaging<Z, T : AbstractPagingObject<Z>>(api: SpotifyAPI, supplier: Supplier<T>) :
        SpotifyRestAction<T>(api, supplier) {

    /**
     * Synchronously retrieve all [AbstractPagingObject] associated with this rest action
     */
    fun getAll() = api.tracks.toAction(Supplier { complete().getAllImpl() })

    /**
     * Synchronously retrieve all [Z] associated with this rest action
     */
    fun getAllItems() = api.tracks.toAction(Supplier { complete().getAllImpl().toList().map { it.items }.flatten() })

    /**
     * Consume each [Z] by [consumer] as it is retrieved
     */
    fun streamAllItems(consumer: (Z) -> Unit): SpotifyRestAction<Unit> {
        return api.tracks.toAction(
                Supplier {
                    complete().getAllImpl().toList().forEach { it.items.forEach { item -> consumer(item) } }
                }
        )
    }
}