/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.http

import java.net.URLEncoder
import java.util.Base64

internal actual fun String.encodeUrl() = URLEncoder.encode(this, "UTF-8")!!
internal actual fun String.base64ByteEncode(): String {
    return Base64.getEncoder().encodeToString(toByteArray())
}
