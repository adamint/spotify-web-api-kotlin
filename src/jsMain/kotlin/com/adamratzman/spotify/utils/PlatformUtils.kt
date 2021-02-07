/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import io.ktor.http.encodeURLQueryComponent
import kotlin.js.Date

internal actual fun String.encodeUrl() = encodeURLQueryComponent()

internal actual fun formatDate(format: String, date: Long): String {
    return Date(date).toISOString()
}

/**
 * Actual platform that this program is run on.
 */
public actual val currentApiPlatform: Platform = Platform.JS

public actual typealias ConcurrentHashMap<K, V> = HashMap<K, V>

public actual fun <K, V> ConcurrentHashMap<K, V>.asList(): List<Pair<K, V>> = toList()
