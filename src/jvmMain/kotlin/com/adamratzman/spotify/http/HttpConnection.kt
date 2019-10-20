/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.http

import kotlinx.coroutines.CoroutineScope

actual fun <T> runBlocking(coroutineCode: suspend CoroutineScope.() -> T): T {
    return kotlinx.coroutines.runBlocking {
        coroutineCode.invoke(this)
    }
}
