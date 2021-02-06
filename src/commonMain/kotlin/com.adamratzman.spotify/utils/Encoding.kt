/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import com.soywiz.krypto.encoding.Base64
import io.ktor.utils.io.core.toByteArray

internal fun String.base64ByteEncode() = Base64.encode(toByteArray())
internal expect fun String.encodeUrl(): String