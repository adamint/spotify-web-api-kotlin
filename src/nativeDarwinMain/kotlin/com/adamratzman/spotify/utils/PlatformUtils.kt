/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import com.soywiz.korio.async.runBlockingNoJs
import io.ktor.http.encodeURLQueryComponent
import kotlinx.coroutines.CoroutineScope

internal actual fun String.encodeUrl() = encodeURLQueryComponent()

/**
 * Actual platform that this program is run on.
 */
public actual val currentApiPlatform: Platform = Platform.NATIVE

public actual typealias ConcurrentHashMap<K, V> = HashMap<K, V>

public actual fun <K, V> ConcurrentHashMap<K, V>.asList(): List<Pair<K, V>> = toList()

public actual fun <T> runBlockingOnJvmAndNative(block: suspend CoroutineScope.() -> T): T {
    return runBlockingNoJs { block() }
}