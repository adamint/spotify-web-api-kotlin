/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
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

        val error = StringBuilder("Spotify Logger ").apply {
            if (fatal) append("FATAL")
            else append("Error")
            if (message != null) {
                append(": ")
                append(message)
            }
            append("\n")
            append(throwable)
        }.toString()
        console.error(error)
    }
}
