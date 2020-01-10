/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

import kotlin.test.Test

actual fun getEnvironmentVariable(name: String): String? {
    console.log(js("process.env"))
    return null
}

actual fun Exception.stackTrace() = println(this)

@Test
fun test() {
    println("asdf")
}
