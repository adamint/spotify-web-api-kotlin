/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

actual class SpotifyLogger actual constructor(actual var enabled: Boolean) {
    val redString = "\u001B[31m"
    val orangeString = "\u001B[33m"
    val resetString = "\u001B[0m"

    actual fun logInfo(message: String) {
        if (enabled) println("Spotify Logger Info: $message")
    }

    actual fun logWarning(message: String) {
        if (enabled) println("${orangeString}Spotify Logger Warning: $message$resetString")
    }

    actual fun logError(fatal: Boolean, message: String?, throwable: Throwable?) {
        if (!enabled) return

        val error = StringBuilder(redString).apply {
            append("Spotify Logger ")
            if (fatal) append("FATAL")
            else append("Error")
            if (message != null) {
                append(": ")
                append(message)
            }
            append(resetString)
        }.toString()
        System.err.println(error)
        throwable?.printStackTrace()
    }
}
