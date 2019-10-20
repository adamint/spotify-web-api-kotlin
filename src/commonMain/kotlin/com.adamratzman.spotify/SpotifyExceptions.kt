/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

open class SpotifyException(message: String, cause: Throwable? = null) : Exception(message, cause)
