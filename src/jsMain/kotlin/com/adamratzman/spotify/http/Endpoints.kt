package com.adamratzman.spotify.http

import com.adamratzman.spotify.utils.encodeToBase64
import io.ktor.http.encodeURLQueryComponent

internal actual fun String.base64ByteEncode(): String {
    return this.encodeToBase64()
}

internal actual fun String.encodeUrl() = encodeURLQueryComponent()
