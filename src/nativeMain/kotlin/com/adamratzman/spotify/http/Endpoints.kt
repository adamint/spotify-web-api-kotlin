/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.http

import com.adamratzman.spotify.utils.encodeToBase64
import io.ktor.http.encodeURLQueryComponent
import kotlin.native.concurrent.ensureNeverFrozen

actual fun SpotifyCache.f() { println("reached");println("after")}
internal actual fun String.base64ByteEncode(): String {
    return this.encodeToBase64()
}

internal actual fun String.encodeUrl() = encodeURLQueryComponent()
