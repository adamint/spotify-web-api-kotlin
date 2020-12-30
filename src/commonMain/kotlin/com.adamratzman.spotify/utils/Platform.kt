/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

public enum class Platform {
    JVM,
    ANDROID,
    JS
}

public expect val platform: Platform
