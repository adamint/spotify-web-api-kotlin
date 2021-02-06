package com.adamratzman.spotify.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date

internal actual fun String.encodeUrl() = URLEncoder.encode(this, "UTF-8")!!

@SuppressLint("SimpleDateFormat")
internal actual fun formatDate(format: String, date: Long): String {
    return SimpleDateFormat(format).format(Date(date))
}

internal actual fun encodeBufferedImageToBase64String(image: BufferedImage): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

internal actual fun convertFileToBufferedImage(file: File): BufferedImage = BitmapFactory.decodeFile(file.absolutePath)

internal actual fun convertLocalImagePathToBufferedImage(path: String): BufferedImage = BitmapFactory.decodeFile(path)

internal actual fun convertUrlPathToBufferedImage(url: String): BufferedImage {
    return URL(url).openConnection().getInputStream().use { inputStream ->
        BitmapFactory.decodeStream(inputStream)
    }
}

/**
 * Actual platform that this program is run on.
 */
public actual val currentApiPlatform: Platform = Platform.ANDROID

public actual typealias ConcurrentHashMap<K, V> = java.util.concurrent.ConcurrentHashMap<K, V>

public actual typealias BufferedImage = Bitmap // TODO

public actual typealias File = java.io.File

public actual fun <K, V> ConcurrentHashMap<K, V>.asList(): List<Pair<K, V>> = toList()
