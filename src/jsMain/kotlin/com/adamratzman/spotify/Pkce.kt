package com.adamratzman.spotify

actual fun getSpotifyPkceCodeChallenge(codeVerifier: String): String = throw NotImplementedError("Only available on JVM/Android")