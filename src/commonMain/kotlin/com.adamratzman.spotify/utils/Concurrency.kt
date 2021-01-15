/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

/**
 * Represents a unit of time
 */
public expect enum class TimeUnit {
    MILLISECONDS, SECONDS;

    public fun toMillis(duration: Long): Long
}
