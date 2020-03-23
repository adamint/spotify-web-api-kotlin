/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import com.adamratzman.spotify.SpotifyRestAction
import java.util.concurrent.CompletableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking as kRunBlocking

internal actual inline fun <T> runBlocking(crossinline coroutineCode: suspend () -> T): T = kRunBlocking {
    coroutineCode()
}

actual typealias TimeUnit = java.util.concurrent.TimeUnit

internal actual fun CoroutineScope.schedule(
    quantity: Int,
    timeUnit: TimeUnit,
    consumer: () -> Unit
) {
    launch {
        delay(timeUnit.toMillis(quantity.toLong()))
        consumer()
    }
}

/**
 * Return this [SpotifyRestAction] as a normal [CompletableFuture]
 */
fun <T> SpotifyRestAction<T>.asFuture(): CompletableFuture<T> = CompletableFuture.supplyAsync(::complete)
