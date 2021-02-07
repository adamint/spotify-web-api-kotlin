/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import android.annotation.SuppressLint
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date

internal actual fun String.encodeUrl() = URLEncoder.encode(this, "UTF-8")!!

@SuppressLint("SimpleDateFormat")
internal actual fun formatDate(format: String, date: Long): String {
    return SimpleDateFormat(format).format(Date(date))
}

/**
 * Actual platform that this program is run on.
 */
public actual val currentApiPlatform: Platform = Platform.ANDROID

public actual typealias ConcurrentHashMap<K, V> = java.util.concurrent.ConcurrentHashMap<K, V>

public actual fun <K, V> ConcurrentHashMap<K, V>.asList(): List<Pair<K, V>> = toList()
