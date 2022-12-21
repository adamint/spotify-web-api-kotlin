/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import kotlinx.coroutines.runBlocking

public actual fun <T> runBlockingOnJvmAndNative(block: suspend () -> T): T {
    return runBlocking { block() }
}
