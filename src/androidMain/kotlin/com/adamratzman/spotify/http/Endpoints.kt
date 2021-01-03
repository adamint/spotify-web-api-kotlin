/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.http

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.Base64
import java.net.URLEncoder

actual fun SpotifyCache.f() {}

internal actual fun String.encodeUrl() = URLEncoder.encode(this, "UTF-8")!!

internal actual fun String.base64ByteEncode(): String {
    return if (VERSION.SDK_INT >= VERSION_CODES.O) {
        java.util.Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(toByteArray())
    } else {
        Base64.encodeToString(toByteArray(), Base64.DEFAULT)
    }
}
