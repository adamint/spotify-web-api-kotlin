package com.adamratzman.spotify

import android.util.Base64
import java.security.MessageDigest

actual fun getSpotifyPkceCodeChallenge(codeVerifier: String): String {
    val sha256 = MessageDigest.getInstance("SHA-256").digest(codeVerifier.toByteArray())
    return Base64.encodeToString(sha256, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
}
