package com.adamratzman.spotify.utils

import org.w3c.files.File

actual typealias BufferedImage = File

actual typealias ConcurrentHashMap<K, V> = HashMap<K, V>

actual fun <K, V> ConcurrentHashMap<K, V>.toList(): List<Pair<K, V>> = toList()