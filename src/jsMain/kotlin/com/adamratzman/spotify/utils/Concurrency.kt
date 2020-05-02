/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.promise

internal actual inline fun <T> runBlocking(crossinline coroutineCode: suspend () -> T): dynamic {
    return GlobalScope.promise { coroutineCode() }
}

actual enum class TimeUnit(val multiplier: Int) {
    MILLISECONDS(1), SECONDS(1000), MINUTES(60000);

    actual fun toMillis(duration: Long) = duration * multiplier
}

internal actual inline fun CoroutineScope.schedule(
    quantity: Int,
    timeUnit: TimeUnit,
    crossinline consumer: () -> Unit
) {
    launch {
        delay(timeUnit.toMillis(quantity.toLong()))
        consumer()
    }
}
