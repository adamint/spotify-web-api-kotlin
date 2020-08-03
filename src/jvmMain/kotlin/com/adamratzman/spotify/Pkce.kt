package com.adamratzman.spotify

import java.security.MessageDigest
import java.util.Base64

actual fun getSpotifyPkceCodeChallenge(codeVerifier: String): String {
    val sha256 = MessageDigest.getInstance("SHA-256").digest(codeVerifier.toByteArray())
    return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(sha256)
}
