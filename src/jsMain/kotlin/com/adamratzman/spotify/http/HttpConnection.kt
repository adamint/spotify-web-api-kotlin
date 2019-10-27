package com.adamratzman.spotify.http

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.Promise

actual fun <T> runBlocking(coroutineCode: suspend () -> T):dynamic {
    return GlobalScope.promise { coroutineCode() }
}