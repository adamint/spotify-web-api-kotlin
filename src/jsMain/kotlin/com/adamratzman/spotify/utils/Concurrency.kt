/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

actual enum class TimeUnit(val multiplier: Int) {
    MILLISECONDS(1), SECONDS(1000), MINUTES(60000)
    ;
    actual fun toMillis(duration: Long) = duration * multiplier
}

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
