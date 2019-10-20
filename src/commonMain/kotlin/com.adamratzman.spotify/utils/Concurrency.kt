package com.adamratzman.spotify.utils

import kotlinx.coroutines.CoroutineScope

expect enum class TimeUnit {
    MILLISECONDS, SECONDS;
    fun toMillis(duration: Long): Long
}

expect fun CoroutineScope.schedule(quantity: Int, timeUnit: TimeUnit, consumer: () -> Unit)