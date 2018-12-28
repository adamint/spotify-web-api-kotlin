/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.main

import com.adamratzman.spotify.utils.PagingObject
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

class SpotifyRestAction<T>(private val api: SpotifyAPI, private val supplier: Supplier<T>) {
    fun complete(): T {
        return try {
            supplier.get()
        } catch (e: Throwable) {
            throw e
        }
    }

    fun queue() = queue({}, { throw it })

    fun queue(consumer: (T) -> Unit) = queue(consumer, {})

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

    fun asFuture() = CompletableFuture.supplyAsync(supplier)

    fun queueAfter(quantity: Int, timeUnit: TimeUnit = TimeUnit.SECONDS, consumer: (T) -> Unit) {
        val runAt = System.currentTimeMillis() + timeUnit.toMillis(quantity.toLong())
        queue { result ->
            api.executor.schedule({ consumer(result) }, runAt - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        }
    }

    override fun toString() = complete().toString()
}

/**
 * Specialized implementation of RestActions just for [PagingObject]. This class gives you the same
 * functionality of [SpotifyRestAction], but in addition, you have the ability to retrieve all linked
 * items or paging objects using [getAllItems] or [getAllPagingObjects], respectively
 */
class SpotifyRestPagingAction<Z, T : PagingObject<Z>>(val api: SpotifyAPI, val supplier: Supplier<T>) {
    private val restAction = SpotifyRestAction(api, supplier)

    /**
     * Block the current thread and get the objects for the **specified** PagingObject
     */
    fun complete(): List<Z> = completeWithPaging().items
    fun queue() = queue({}, { throw it })
    fun queue(consumer: (List<Z>) -> Unit) = queue(consumer, {})
    fun queue(consumer: ((List<Z>) -> Unit), failure: ((Throwable) -> Unit)) =
            queueWithPaging({ consumer(it.items) }, failure)

    fun queueAfter(quantity: Int, timeUnit: TimeUnit = TimeUnit.SECONDS, consumer: (List<Z>) -> Unit) =
            queueAfterWithPaging(quantity, timeUnit) { consumer(it.items) }

    fun asFuture(): CompletableFuture<List<Z>> = asFutureWithPaging().thenApply { it.items }

    fun completeWithPaging(): T = restAction.complete()
    fun asFutureWithPaging(): CompletableFuture<T> = restAction.asFuture()
    fun queueWithPaging() = restAction.queue({}, { throw it })
    fun queueWithPaging(consumer: (T) -> Unit) = restAction.queue(consumer, {})
    fun queueWithPaging(consumer: ((T) -> Unit), failure: ((Throwable) -> Unit)) = restAction.queue(consumer, failure)
    fun queueAfterWithPaging(quantity: Int, timeUnit: TimeUnit = TimeUnit.SECONDS, consumer: (T) -> Unit) = restAction.queueAfter(quantity, timeUnit, consumer)

    fun getAllPagingObjects() = SpotifyRestAction(api, Supplier { restAction.complete().getAll().complete() })

    /**
     * Get the items for **all linked [PagingObjects]**
     */
    fun getAllItems(): SpotifyRestAction<List<Z>> = SpotifyRestAction(api, Supplier { getAllPagingObjects().complete().map { it.items }.flatten() })
}