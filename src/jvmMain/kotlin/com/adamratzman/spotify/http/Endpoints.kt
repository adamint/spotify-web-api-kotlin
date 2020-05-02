/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.http

import java.net.URLEncoder
import javax.xml.bind.DatatypeConverter

internal actual fun String.encodeUrl() = URLEncoder.encode(this, "UTF-8")!!
internal actual fun String.base64ByteEncode(): String {
    return DatatypeConverter.printBase64Binary(toByteArray())
}
