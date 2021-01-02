/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import java.io.ByteArrayOutputStream
import java.net.URL
import java.util.Base64
import javax.imageio.ImageIO

internal actual fun encodeBufferedImageToBase64String(image: BufferedImage): String {
    val bos = ByteArrayOutputStream()
    ImageIO.write(image, "jpg", bos)
    bos.close()
    return Base64.getEncoder().encodeToString(bos.toByteArray())
}

internal actual fun convertFileToBufferedImage(file: File): BufferedImage = ImageIO.read(file)

internal actual fun convertLocalImagePathToBufferedImage(path: String): BufferedImage = ImageIO.read(URL("file:///$path"))

internal actual fun convertUrlPathToBufferedImage(url: String): BufferedImage = ImageIO.read(URL(url))
