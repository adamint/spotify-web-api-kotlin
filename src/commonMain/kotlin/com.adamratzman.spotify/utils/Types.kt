/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

public expect class ConcurrentHashMap<K, V>() {
    public operator fun get(key: K): V?
    public fun put(key: K, value: V): V?
    public fun remove(key: K): V?
    public fun clear()

    public val size: Int
    public val entries: MutableSet<MutableMap.MutableEntry<K, V>>
}

public expect fun <K, V> ConcurrentHashMap<K, V>.asList(): List<Pair<K, V>>

public expect class BufferedImage

public expect class File
