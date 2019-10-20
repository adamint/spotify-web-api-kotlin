package com.adamratzman.spotify.utils

actual typealias ConcurrentHashMap<K, V> = java.util.concurrent.ConcurrentHashMap<K, V>

actual typealias BufferedImage = java.awt.image.BufferedImage

actual typealias File = java.io.File

actual fun <K, V> ConcurrentHashMap<K, V>.toList(): List<Pair<K, V>> {
    return this.toList()
}
