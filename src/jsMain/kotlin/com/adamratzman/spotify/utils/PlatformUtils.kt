/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import com.adamratzman.spotify.SpotifyRestAction
import io.ktor.http.encodeURLQueryComponent
import kotlin.js.Promise
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

internal actual fun String.encodeUrl() = encodeURLQueryComponent()

/**
 * Actual platform that this program is run on.
 */
public actual val currentApiPlatform: Platform = Platform.JS

public actual typealias ConcurrentHashMap<K, V> = HashMap<K, V>

public actual fun <K, V> ConcurrentHashMap<K, V>.asList(): List<Pair<K, V>> = toList()

public actual fun <T> runBlockingOnJvmAndNative(block: suspend () -> T): T {
    throw IllegalStateException("JS does not have runBlocking")
}

public fun <T> SpotifyRestAction<T>.asPromise(): Promise<T> = GlobalScope.promise {
    supplier()
}
