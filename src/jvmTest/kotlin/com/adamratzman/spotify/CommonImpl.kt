/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

actual fun getEnvironmentVariable(name: String): String? {
    return System.getProperty(name)
}
