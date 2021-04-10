package com.adamratzman.spotify.utils

import kotlinx.coroutines.runBlocking

public actual fun <T> runBlockingOnJvmAndNative(block: suspend () -> T): T {
    return runBlocking { block() }
}
