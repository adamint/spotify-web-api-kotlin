/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

import java.security.MessageDigest
import java.util.Base64

public actual fun getSpotifyPkceCodeChallenge(codeVerifier: String): String {
    val sha256 = MessageDigest.getInstance("SHA-256").digest(codeVerifier.toByteArray())
    return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(sha256)
}
