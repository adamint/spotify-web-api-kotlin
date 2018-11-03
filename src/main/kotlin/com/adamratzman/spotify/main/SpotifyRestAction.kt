package com.adamratzman.spotify.main

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

    fun queue(consumer: ((T) -> Unit)) = queue(consumer, {})

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