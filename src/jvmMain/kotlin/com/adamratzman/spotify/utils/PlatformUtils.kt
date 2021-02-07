/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

internal actual fun String.encodeUrl() = URLEncoder.encode(this, "UTF-8")!!

internal actual fun formatDate(format: String, date: Long): String {
    return SimpleDateFormat(format, Locale.getDefault()).format(Date.from(Instant.ofEpochMilli(date)))
}

/**
 * The actual platform that this program is running on.
 */
public actual val currentApiPlatform: Platform = Platform.JVM

public actual typealias ConcurrentHashMap<K, V> = java.util.concurrent.ConcurrentHashMap<K, V>

public actual fun <K, V> ConcurrentHashMap<K, V>.asList(): List<Pair<K, V>> = toList()
