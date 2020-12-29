/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

import kotlin.coroutines.CoroutineContext
import kotlin.test.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

val clientId = getEnvironmentVariable("SPOTIFY_CLIENT_ID")
val clientSecret = getEnvironmentVariable("SPOTIFY_CLIENT_SECRET")
val redirectUri = getEnvironmentVariable("SPOTIFY_REDIRECT_URI")
val tokenString = getEnvironmentVariable("SPOTIFY_TOKEN_STRING")

var instantiationCompleted: Boolean = false
private lateinit var apiBacking: GenericSpotifyApi

// https://github.com/Kotlin/kotlinx.coroutines/issues/706#issuecomment-429922811
val spotifyApi: Deferred<GenericSpotifyApi?>
    get() = GlobalScope.async(Dispatchers.Unconfined, start = LAZY) {
        if (::apiBacking.isInitialized) return@async apiBacking
        when {
            tokenString?.isNotBlank() == true -> {
                spotifyClientApi {
                    credentials {
                        clientId = com.adamratzman.spotify.clientId
                        clientSecret = com.adamratzman.spotify.clientSecret
                        redirectUri = com.adamratzman.spotify.redirectUri
                    }
                    authorization {
                        tokenString = com.adamratzman.spotify.tokenString
                    }
                }.build().also { instantiationCompleted = true; apiBacking = it }
            }
            clientId?.isNotBlank() == true -> {
                spotifyAppApi {
                    credentials {
                        clientId = com.adamratzman.spotify.clientId
                        clientSecret = com.adamratzman.spotify.clientSecret
                    }
                }.build().also { instantiationCompleted = true; apiBacking = it }
            }
            else -> null.also { instantiationCompleted = true }
        }
    }

expect fun getEnvironmentVariable(name: String): String?

expect fun Exception.stackTrace()

// https://github.com/Kotlin/kotlinx.coroutines/issues/1996#issuecomment-728562784
expect fun runBlockingTest(block: suspend CoroutineScope.() -> Unit)
expect val testCoroutineContext: CoroutineContext

suspend inline fun <reified T : Throwable> assertFailsWithSuspend(crossinline block: suspend () -> Unit) {
    val noExceptionMessage = "Expected ${T::class.simpleName} exception to be thrown, but no exception was thrown."
    try {
        block()
        throw AssertionError(noExceptionMessage)
    } catch (exception: Throwable) {
        if (exception.message == noExceptionMessage) throw exception
        assertTrue(exception is T, "Expected ${T::class.simpleName} exception to be thrown, but exception ${exception::class.simpleName} (${exception.message}) was thrown.")
    }
}
