/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import com.soywiz.klock.DateTime
import kotlin.system.getTimeMillis

/**
 * The current time in milliseconds since UNIX epoch.
 */
public actual fun getCurrentTimeMs(): Long {
    return getTimeMillis()
}

internal actual fun formatDate(format: String, date: Long): String {
    return DateTime.now().toString("yyyy-MM-dd'T'HH:mm:ss")
}
