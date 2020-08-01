/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

actual typealias ConcurrentHashMap<K, V> = java.util.concurrent.ConcurrentHashMap<K, V>

actual typealias BufferedImage = String // TODO

actual typealias File = java.io.File

actual fun <K, V> ConcurrentHashMap<K, V>.asList(): List<Pair<K, V>> = toList()
