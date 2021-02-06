/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import io.ktor.http.encodeURLQueryComponent

internal actual fun String.encodeUrl() = encodeURLQueryComponent()

public actual typealias File = String

internal actual fun convertFileToBufferedImage(file: File): BufferedImage = file
internal actual fun convertUrlPathToBufferedImage(url: String): BufferedImage =
    throw NotImplementedError("Images not implemented yet.")

internal actual fun convertLocalImagePathToBufferedImage(path: String): BufferedImage =
    throw NotImplementedError("Images not implemented yet.")

internal actual fun encodeBufferedImageToBase64String(image: BufferedImage): String =
    throw NotImplementedError("Images not implemented yet.")

/**
 * Actual platform that this program is run on.
 */
public actual val currentApiPlatform: Platform = Platform.NATIVE

public actual typealias BufferedImage = String

public actual typealias ConcurrentHashMap<K, V> = HashMap<K, V>

public actual fun <K, V> ConcurrentHashMap<K, V>.asList(): List<Pair<K, V>> = toList()
