/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

actual fun getEnvironmentVariable(name: String): String? {
    console.log(js("process.env"))
    return null
}

actual fun Exception.stackTrace() = println(this)

fun main() {
    println(js("process.env"))
}
