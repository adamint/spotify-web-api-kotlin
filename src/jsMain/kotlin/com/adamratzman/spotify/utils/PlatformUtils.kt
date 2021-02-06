package com.adamratzman.spotify.utils

import io.ktor.http.encodeURLQueryComponent
import org.w3c.files.File
import kotlin.js.Date

internal actual fun String.encodeUrl() = encodeURLQueryComponent()

internal actual fun formatDate(format: String, date: Long): String {
    return Date(date).toISOString()
}

public actual typealias File = File

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
public actual val currentApiPlatform: Platform = Platform.JS

public actual typealias BufferedImage = File

public actual typealias ConcurrentHashMap<K, V> = HashMap<K, V>

public actual fun <K, V> ConcurrentHashMap<K, V>.asList(): List<Pair<K, V>> = toList()

