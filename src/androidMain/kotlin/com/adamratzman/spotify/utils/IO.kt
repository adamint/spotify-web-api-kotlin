/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.net.URL

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
