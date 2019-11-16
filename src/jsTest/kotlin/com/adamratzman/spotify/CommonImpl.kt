/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

import kotlin.test.Test

actual fun getEnvironmentVariable(name: String): String? {
    console.log(js("process.env"))
    return null
}

@Test
fun test() {
    println("asdf")
}
