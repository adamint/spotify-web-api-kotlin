/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking

actual fun getEnvironmentVariable(name: String): String? {
    return System.getenv(name) ?: System.getProperty(name)
}

actual fun Exception.stackTrace() {
    println(this.stackTrace.joinToString("\n") { it.toString() })
    this.printStackTrace()
}

actual val testCoroutineContext: CoroutineContext =
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()

actual fun runBlockingTest(block: suspend CoroutineScope.() -> Unit) = runBlocking(testCoroutineContext) { this.block() }
