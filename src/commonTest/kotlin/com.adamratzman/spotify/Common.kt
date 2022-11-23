/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify

import kotlin.test.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

expect fun areLivePkceTestsEnabled(): Boolean
expect fun arePlayerTestsEnabled(): Boolean
expect fun getTestClientId(): String?
expect fun getTestClientSecret(): String?
expect fun getTestRedirectUri(): String?
expect fun getTestTokenString(): String?
expect fun isHttpLoggingEnabled(): Boolean

expect suspend fun buildSpotifyApi(): GenericSpotifyApi?

suspend inline fun <reified T : Throwable> assertFailsWithSuspend(crossinline block: suspend () -> Unit) {
    val noExceptionMessage = "Expected ${T::class.simpleName} exception to be thrown, but no exception was thrown."
    try {
        block()
        throw AssertionError(noExceptionMessage)
    } catch (exception: Throwable) {
        if (exception.message == noExceptionMessage) throw exception
        assertTrue(
            exception is T,
            "Expected ${T::class.simpleName} exception to be thrown, but exception ${exception::class.simpleName} (${exception.message}) was thrown."
        )
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> runTestOnDefaultDispatcher(block: suspend CoroutineScope.() -> T): TestResult = runTest {
    withContext(Dispatchers.Default) {
        block()
    }
}