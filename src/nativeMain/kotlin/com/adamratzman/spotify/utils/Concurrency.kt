/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

@Suppress("unused")
public actual enum class TimeUnit(public val multiplier: Int) {
    MILLISECONDS(1), SECONDS(1000), MINUTES(60000);

    public actual fun toMillis(duration: Long): Long = duration * multiplier
}
