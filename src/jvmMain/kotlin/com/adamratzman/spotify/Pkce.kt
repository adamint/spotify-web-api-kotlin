/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify

import java.security.MessageDigest
import java.util.Base64

/**
 * A utility to get the pkce code challenge for a corresponding code verifier. Only available on JVM/Android
 */
public actual fun getSpotifyPkceCodeChallenge(codeVerifier: String): String {
    val sha256 = MessageDigest.getInstance("SHA-256").digest(codeVerifier.toByteArray())
    return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(sha256)
}
