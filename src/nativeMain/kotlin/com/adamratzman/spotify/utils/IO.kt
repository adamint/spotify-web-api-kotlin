/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import platform.posix.__sFILE

public actual typealias File = __sFILE

internal actual fun convertFileToBufferedImage(file: File): BufferedImage = file
internal actual fun convertUrlPathToBufferedImage(url: String): BufferedImage =
        throw NotImplementedError("Images not implemented yet.")
internal actual fun convertLocalImagePathToBufferedImage(path: String): BufferedImage =
        throw NotImplementedError("Images not implemented yet.")
internal actual fun encodeBufferedImageToBase64String(image: BufferedImage): String =
        throw NotImplementedError("Images not implemented yet.")
