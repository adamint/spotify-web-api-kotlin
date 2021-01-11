/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import kotlin.system.getTimeMillis

public actual fun getCurrentTimeMs(): Long {
    return getTimeMillis()
}
