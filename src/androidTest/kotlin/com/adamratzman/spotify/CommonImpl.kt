/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify

import android.os.Build.VERSION
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking

private fun setFinalStatic(field: Field, newValue: Any?) {
    field.isAccessible = true
    val modifiersField = Field::class.java.getDeclaredField("modifiers")
    modifiersField.isAccessible = true
    modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
    field.set(null, newValue)
}

private fun getEnvironmentVariable(name: String): String? {
    setFinalStatic(VERSION::class.java.getField("SDK_INT"), 26)
    return System.getenv(name) ?: System.getProperty(name)
}

actual fun getTestClientId(): String? = getEnvironmentVariable("SPOTIFY_CLIENT_ID")
actual fun getTestClientSecret(): String? = getEnvironmentVariable("SPOTIFY_CLIENT_SECRET")
actual fun getTestRedirectUri(): String? = getEnvironmentVariable("SPOTIFY_REDIRECT_URI")
actual fun getTestTokenString(): String? = getEnvironmentVariable("SPOTIFY_TOKEN_STRING")
actual fun isHttpLoggingEnabled(): Boolean = getEnvironmentVariable("SPOTIFY_LOG_HTTP") == "true"
actual fun arePlayerTestsEnabled(): Boolean = getEnvironmentVariable("SPOTIFY_ENABLE_PLAYER_TESTS")?.toBoolean() == true
actual fun areLivePkceTestsEnabled(): Boolean = getEnvironmentVariable("VERBOSE_TEST_ENABLED")?.toBoolean() ?: false

actual suspend fun buildSpotifyApi(): GenericSpotifyApi? {
    val clientId = getTestClientId()
    val clientSecret = getTestClientSecret()
    val tokenString = getTestTokenString()
    val logHttp = isHttpLoggingEnabled()

    return when {
        tokenString?.isNotBlank() == true -> {
            spotifyClientApi {
                credentials {
                    this.clientId = clientId
                    this.clientSecret = clientSecret
                    this.redirectUri = getTestRedirectUri()
                }
                authorization {
                    this.tokenString = tokenString
                }
                options {
                    this.enableDebugMode = logHttp
                }
            }.build()
        }

        clientId?.isNotBlank() == true -> {
            spotifyAppApi {
                credentials {
                    this.clientId = clientId
                    this.clientSecret = clientSecret
                }
                options {
                    this.enableDebugMode = logHttp
                }
            }.build()
        }

        else -> null
    }
}