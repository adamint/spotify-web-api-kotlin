/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

/**
 * Represents a unit of time
 */
public enum class TimeUnit(private val multiplier: Long) {
    Milliseconds(1),
    Seconds(1000),
    Minutes(60000);

    public fun toMillis(duration: Long): Long = duration * multiplier
}
