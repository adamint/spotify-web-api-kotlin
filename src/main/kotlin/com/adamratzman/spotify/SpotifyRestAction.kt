/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.models.AbstractPagingObject
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

open class SpotifyRestAction<T>(protected val api: SpotifyAPI, private val supplier: Supplier<T>) {
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

class SpotifyRestActionPaging<Z, T : AbstractPagingObject<Z>>(api: SpotifyAPI, supplier: Supplier<T>) :
    SpotifyRestAction<T>(api, supplier) {
    fun getAll() = api.tracks.toAction(Supplier { complete().getAllImpl() })
    fun getAllItems() = api.tracks.toAction(Supplier { complete().getAllImpl().toList().map { it.items }.flatten() })
}