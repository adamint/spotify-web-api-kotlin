/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

import android.os.Build.VERSION
import java.lang.reflect.Field
import java.lang.reflect.Modifier

fun setFinalStatic(field: Field, newValue: Any?) {
    field.isAccessible = true
    val modifiersField = Field::class.java.getDeclaredField("modifiers")
    modifiersField.isAccessible = true
    modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
    field.set(null, newValue)
}

actual fun getEnvironmentVariable(name: String): String? {
    setFinalStatic(VERSION::class.java.getField("SDK_INT"), 26)
    return System.getenv(name) ?: System.getProperty(name)
}

actual fun Exception.stackTrace() {
    println(this.stackTrace.joinToString("\n") { it.toString() })
    this.printStackTrace()
}
