/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import com.adamratzman.spotify.SpotifyRestAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.CompletableFuture

actual typealias TimeUnit = java.util.concurrent.TimeUnit

actual fun CoroutineScope.schedule(
    quantity: Int,
    timeUnit: TimeUnit,
    consumer: () -> Unit
) {
    launch {
        delay(timeUnit.toMillis(quantity.toLong()))
        consumer()
    }
}

fun <T> SpotifyRestAction<T>.asFuture() = CompletableFuture.supplyAsync(supplier)
