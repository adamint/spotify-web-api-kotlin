package com.adamratzman.spotify.utils

internal expect fun encodeBufferedImageToBase64String(image: BufferedImage): String

internal expect fun convertFileToBufferedImage(file: File): BufferedImage
internal expect fun convertUrlPathToBufferedImage(url: String): BufferedImage
internal expect fun convertLocalImagePathToBufferedImage(path: String): BufferedImage

