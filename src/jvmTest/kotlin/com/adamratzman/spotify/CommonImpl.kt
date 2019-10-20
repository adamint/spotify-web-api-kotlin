/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

actual fun getEnvironmentVariable(name: String): String? {
    println("$name ${System.getProperty(name)} ${System.getenv(name)}")
    return System.getProperty(name)
}
