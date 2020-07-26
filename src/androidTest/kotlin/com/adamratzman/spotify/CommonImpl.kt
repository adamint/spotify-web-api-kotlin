/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

actual fun getEnvironmentVariable(name: String): String? {
    return System.getenv(name) ?: System.getProperty(name)
}

actual fun Exception.stackTrace() {
    println(this.stackTrace.joinToString("\n") { it.toString() })
    this.printStackTrace()
}
