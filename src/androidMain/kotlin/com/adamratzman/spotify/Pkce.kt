/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.Base64
import java.security.MessageDigest

public actual fun getSpotifyPkceCodeChallenge(codeVerifier: String): String {
    val sha256 = MessageDigest.getInstance("SHA-256").digest(codeVerifier.toByteArray())
    return if (VERSION.SDK_INT >= VERSION_CODES.O) {
        java.util.Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(sha256)
    } else {
        Base64.encodeToString(sha256, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    }
}
