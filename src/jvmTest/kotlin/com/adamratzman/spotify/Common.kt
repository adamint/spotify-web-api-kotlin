package com.adamratzman.spotify

actual fun getEnvironmentalVariable(name: String): String? {
    return System.getenv(name)
}