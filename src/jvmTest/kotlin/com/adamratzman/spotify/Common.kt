package com.adamratzman.spotify

actual fun getEnvironmentVariable(name: String): String? {
    return System.getenv(name)
}