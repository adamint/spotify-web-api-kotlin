/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify

actual fun areLivePkceTestsEnabled(): Boolean = false
actual fun arePlayerTestsEnabled(): Boolean = false

actual fun getTestClientId(): String? = null
actual fun getTestClientSecret(): String? = null
actual suspend fun buildSpotifyApi(): GenericSpotifyApi? = null