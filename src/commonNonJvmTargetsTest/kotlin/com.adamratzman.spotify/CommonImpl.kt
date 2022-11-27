/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify

actual fun areLivePkceTestsEnabled(): Boolean = false
actual fun arePlayerTestsEnabled(): Boolean = false
actual fun isHttpLoggingEnabled(): Boolean = false
actual fun getTestTokenString(): String? = null
actual fun getTestRedirectUri(): String? = null
actual fun getTestClientId(): String? = null
actual fun getTestClientSecret(): String? = null
actual fun getResponseCacher(): ResponseCacher? = null

actual suspend fun buildSpotifyApi(testClassQualifiedName: String, testName: String): GenericSpotifyApi? = null
