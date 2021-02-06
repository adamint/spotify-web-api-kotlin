/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

/**
 * Represents a unit of time
 */
public enum class TimeUnit(public val multiplier: Long) {
    MILLISECONDS(1), SECONDS(1000), MINUTES(60000);

    public fun toMillis(duration: Long): Long = duration * multiplier
}
