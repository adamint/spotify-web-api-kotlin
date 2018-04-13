package com.adamratzman.spotify.utils

import com.adamratzman.spotify.main.SpotifyAPI
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

    fun queue(consumer: (T) -> Unit) = api.executor.execute { consumer(complete()) }
    fun asFuture() = CompletableFuture.supplyAsync(supplier)
    fun queueAfter(quantity: Int, timeUnit: TimeUnit = TimeUnit.SECONDS, consumer: (T) -> Unit) {
        val runAt = System.currentTimeMillis() + timeUnit.toMillis(quantity.toLong())
        val result = complete()
        api.executor.schedule({ consumer(result) }, runAt - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
    }
}