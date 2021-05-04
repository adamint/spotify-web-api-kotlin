/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import java.net.URLEncoder
import kotlinx.coroutines.runBlocking

internal actual fun String.encodeUrl() = URLEncoder.encode(this, "UTF-8")!!

/**
 * The actual platform that this program is running on.
 */
public actual val currentApiPlatform: Platform = Platform.JVM

public actual typealias ConcurrentHashMap<K, V> = java.util.concurrent.ConcurrentHashMap<K, V>

public actual fun <K, V> ConcurrentHashMap<K, V>.asList(): List<Pair<K, V>> = toList()

public actual fun <T> runBlockingOnJvmAndNative(block: suspend () -> T): T {
    return runBlocking { block() }
}
