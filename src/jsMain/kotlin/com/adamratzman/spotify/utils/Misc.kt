/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import kotlin.js.Date

internal actual fun getCurrentTimeMs(): Long {
    return Date().getTime().toLong()
}

internal actual fun formatDate(format: String, date: Long): String {
    return Date(date).toISOString()
}
