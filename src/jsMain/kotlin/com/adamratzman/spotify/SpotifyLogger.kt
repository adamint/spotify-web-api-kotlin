/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

actual class SpotifyLogger actual constructor(actual var enabled: Boolean) {
    actual fun logInfo(message: String) {
        if (enabled) console.log(message)
    }

    actual fun logWarning(message: String) {
        if (enabled) console.log("Warning: $message")
    }

    actual fun logError(fatal: Boolean, message: String?, throwable: Throwable?) {
        if (!enabled) return

        val sb = StringBuilder("Spotify Logger ")
        sb.append(if (fatal) "FATAL" else "Error")
        if (message != null) sb.append(": $message")
        sb.append("\n$throwable")
    }
}
