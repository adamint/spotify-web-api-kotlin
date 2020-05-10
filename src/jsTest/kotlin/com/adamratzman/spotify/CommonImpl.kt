/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

actual fun getEnvironmentVariable(name: String): String? {
    println("env $name: " + process.env[name].unsafeCast<String?>())
    return process.env[name].unsafeCast<String?>()
}

actual fun Exception.stackTrace() = println(this)


external val process: Process

external interface Process {
    val env: dynamic
}

fun main() {
    println(api.token)
    println(api.browse.getAvailableGenreSeeds().complete())
}