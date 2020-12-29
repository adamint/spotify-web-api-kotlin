/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

public expect enum class TimeUnit {
    MILLISECONDS, SECONDS;

    public fun toMillis(duration: Long): Long
}
