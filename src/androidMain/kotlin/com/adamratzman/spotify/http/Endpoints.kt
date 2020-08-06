/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.http

import android.util.Base64
import java.net.URLEncoder

internal actual fun String.encodeUrl() = URLEncoder.encode(this, "UTF-8")!!
internal actual fun String.base64ByteEncode(): String {
    return Base64.encodeToString(toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
}
