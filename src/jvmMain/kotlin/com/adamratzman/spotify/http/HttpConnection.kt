/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.http

actual fun <T> runBlocking(coroutineCode: suspend () -> T): T {
    return kotlinx.coroutines.runBlocking {
        coroutineCode()
    }
}
