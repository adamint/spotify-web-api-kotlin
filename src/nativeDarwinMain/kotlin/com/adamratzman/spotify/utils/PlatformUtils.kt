/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import io.ktor.http.encodeURLQueryComponent
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

internal actual fun String.encodeUrl() = encodeURLQueryComponent()

/**
 * Actual platform that this program is run on.
 */
public actual val currentApiPlatform: Platform = Platform.NATIVE

public actual typealias ConcurrentHashMap<K, V> = HashMap<K, V>

public actual fun <K, V> ConcurrentHashMap<K, V>.asList(): List<Pair<K, V>> = toList()

/**
 * The current time in milliseconds since UNIX epoch.
 */
public actual fun getCurrentTimeMs(): Long = Clock.System.now().toEpochMilliseconds()

/**
 * Format date to ISO 8601 format
 */
internal actual fun formatDate(date: Long): String {
    return Instant.fromEpochMilliseconds(date).toString()
}
