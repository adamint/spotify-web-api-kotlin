/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import java.io.ByteArrayOutputStream
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Base64
import java.util.Date
import java.util.Locale
import javax.imageio.ImageIO

internal actual fun String.encodeUrl() = URLEncoder.encode(this, "UTF-8")!!

internal actual fun formatDate(format: String, date: Long): String {
    return SimpleDateFormat(format, Locale.getDefault()).format(Date.from(Instant.ofEpochMilli(date)))
}

internal actual fun encodeBufferedImageToBase64String(image: BufferedImage): String {
    val bos = ByteArrayOutputStream()
    ImageIO.write(image, "jpg", bos)
    bos.close()
    return Base64.getEncoder().encodeToString(bos.toByteArray())
}

internal actual fun convertFileToBufferedImage(file: File): BufferedImage = ImageIO.read(file)

internal actual fun convertLocalImagePathToBufferedImage(path: String): BufferedImage =
    ImageIO.read(URL("file:///$path"))

internal actual fun convertUrlPathToBufferedImage(url: String): BufferedImage = ImageIO.read(URL(url))

/**
 * The actual platform that this program is running on.
 */
public actual val currentApiPlatform: Platform = Platform.JVM

public actual typealias ConcurrentHashMap<K, V> = java.util.concurrent.ConcurrentHashMap<K, V>

public actual typealias BufferedImage = java.awt.image.BufferedImage

public actual typealias File = java.io.File

public actual fun <K, V> ConcurrentHashMap<K, V>.asList(): List<Pair<K, V>> = toList()
