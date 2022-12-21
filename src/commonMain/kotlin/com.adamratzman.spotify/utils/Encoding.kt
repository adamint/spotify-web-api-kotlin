/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import com.soywiz.krypto.encoding.Base64
import io.ktor.utils.io.core.toByteArray

internal fun String.base64ByteEncode() = Base64.encode(toByteArray())

public fun String.urlEncodeBase64String(): String {
    var result = this
    while (result.endsWith("=")) result = result.removeSuffix("=")

    return result.replace("/", "_").replace("+", "-")
}

internal expect fun String.encodeUrl(): String
