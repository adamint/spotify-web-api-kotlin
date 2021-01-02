/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify

public expect class SpotifyLogger(enabled: Boolean) {
    public var enabled: Boolean

    public fun logInfo(message: String)
    public fun logWarning(message: String)
    public fun logError(fatal: Boolean, message: String?, throwable: Throwable?)
}
