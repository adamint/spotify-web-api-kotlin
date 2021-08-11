/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise

actual fun getEnvironmentVariable(name: String): String? {
    return process.env[name].unsafeCast<String?>()
}

actual fun Exception.stackTrace() = println(this)

external val process: Process

external interface Process {
    val env: dynamic
}

actual fun runBlockingTest(block: suspend CoroutineScope.() -> Unit): dynamic = GlobalScope.promise { block() }
