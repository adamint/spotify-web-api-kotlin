/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import kotlinx.datetime.Instant

/**
 * The current time in milliseconds since UNIX epoch.
 */
public actual fun getCurrentTimeMs(): Long = System.currentTimeMillis()

/**
 * Format date to ISO 8601 format
 */
internal actual fun formatDate(date: Long): String {
    return Instant.fromEpochMilliseconds(date).toString()
}
