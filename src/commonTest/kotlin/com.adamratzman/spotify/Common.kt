/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.http.HttpRequest
import com.adamratzman.spotify.http.HttpResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlin.test.assertTrue

expect fun areLivePkceTestsEnabled(): Boolean
expect fun arePlayerTestsEnabled(): Boolean
expect fun getTestClientId(): String?
expect fun getTestClientSecret(): String?
expect fun getTestRedirectUri(): String?
expect fun getTestTokenString(): String?
expect fun isHttpLoggingEnabled(): Boolean
expect suspend fun buildSpotifyApi(testClassQualifiedName: String, testName: String): GenericSpotifyApi?
expect fun getResponseCacher(): ResponseCacher?

interface ResponseCacher {
    val cachedResponsesDirectoryPath: String
    fun cacheResponse(className: String, testName: String, responseNumber: Int, request: HttpRequest, response: HttpResponse)
}

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

@Serializable
data class CachedResponse(val request: Request, val response: Response)

@Serializable
data class Request(val url: String, val method: String, val body: String? = null)

@Serializable
data class Response(val responseCode: Int, val headers: Map<String, String>, val body: String)
