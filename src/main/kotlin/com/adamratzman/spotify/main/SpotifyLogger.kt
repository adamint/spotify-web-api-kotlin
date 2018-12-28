/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.main

class SpotifyLogger(var enabled: Boolean) {
    val redString = "\u001B[31m"
    val resetString = "\u001B[0m"
    fun logInfo(message: String) = println("Spotify Logger Info: $message")
    fun logWarning(message: String) = println("${redString}Spotify Logger Warning: $message$resetString")
    fun logError(fatal: Boolean, message: String?, throwable: Throwable?) {
        val sb = StringBuilder("${redString}Spotify Logger ")
        sb.append(if (fatal) "FATAL" else "Error")
        if (message != null) sb.append(": $message")
        sb.append(resetString)
        println(sb)
        throwable?.printStackTrace()
    }
}

class SpotifyException(message: String, cause: Throwable) : Exception(message, cause)