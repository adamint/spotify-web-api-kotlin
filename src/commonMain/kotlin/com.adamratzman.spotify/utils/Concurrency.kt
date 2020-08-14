/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import kotlinx.coroutines.CoroutineScope

expect enum class TimeUnit {
    MILLISECONDS, SECONDS;

    fun toMillis(duration: Long): Long
}

internal expect fun CoroutineScope.schedule(quantity: Int, timeUnit: TimeUnit, consumer: () -> Unit)

expect fun <T> runBlockingMpp(coroutineCode: suspend () -> T): T
