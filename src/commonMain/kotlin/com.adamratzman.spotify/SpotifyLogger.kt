/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

expect class SpotifyLogger(enabled: Boolean) {
    var enabled: Boolean

    fun logInfo(message: String)
    fun logWarning(message: String)
    fun logError(fatal: Boolean, message: String?, throwable: Throwable?)
}
