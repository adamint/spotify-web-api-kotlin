/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

internal actual fun encodeBufferedImageToBase64String(image: BufferedImage): String {
    /*val bos = ByteArrayOutputStream()
    ImageIO.write(image, "jpg", bos)
    bos.close()
    return Base64.getEncoder().encodeToString(bos.toByteArray())*/
    throw NotImplementedError()
}

internal actual fun convertFileToBufferedImage(file: File): BufferedImage = throw NotImplementedError()

internal actual fun convertLocalImagePathToBufferedImage(path: String): BufferedImage = throw NotImplementedError()

internal actual fun convertUrlPathToBufferedImage(url: String): BufferedImage = throw NotImplementedError()
