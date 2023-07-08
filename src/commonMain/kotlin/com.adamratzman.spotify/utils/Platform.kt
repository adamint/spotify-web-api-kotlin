/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

/**
 * Actual platforms that this program can be run on.
 */
public enum class Platform {
    Jvm,
    Android,
    Js,
    Native
}

public expect val currentApiPlatform: Platform
