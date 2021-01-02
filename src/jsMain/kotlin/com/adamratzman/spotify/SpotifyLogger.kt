/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify

public actual class SpotifyLogger actual constructor(public actual var enabled: Boolean) {
    public actual fun logInfo(message: String) {
        if (enabled) console.log(message)
    }

    public actual fun logWarning(message: String) {
        if (enabled) console.log("Warning: $message")
    }

    public actual fun logError(fatal: Boolean, message: String?, throwable: Throwable?) {
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
