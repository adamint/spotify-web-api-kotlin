/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify

import android.os.Build.VERSION
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking

public fun setFinalStatic(field: Field, newValue: Any?) {
    field.isAccessible = true
    val modifiersField = Field::class.java.getDeclaredField("modifiers")
    modifiersField.isAccessible = true
    modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
    field.set(null, newValue)
}

public actual fun getEnvironmentVariable(name: String): String? {
    setFinalStatic(VERSION::class.java.getField("SDK_INT"), 26)
    return System.getenv(name) ?: System.getProperty(name)
}

public actual fun Exception.stackTrace() {
    println(this.stackTrace.joinToString("\n") { it.toString() })
    this.printStackTrace()
}

public actual val testCoroutineContext: CoroutineContext =
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()

public actual fun runBlockingTest(block: suspend CoroutineScope.() -> Unit): Unit = runBlocking(testCoroutineContext) { this.block() }
