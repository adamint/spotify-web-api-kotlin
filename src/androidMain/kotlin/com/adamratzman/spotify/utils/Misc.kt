/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

/**
 * The current time in milliseconds since UNIX epoch.
 */
public actual fun getCurrentTimeMs(): Long = System.currentTimeMillis()

@SuppressLint("SimpleDateFormat")
internal actual fun formatDate(format: String, date: Long): String {
    return SimpleDateFormat(format).format(Date(date))
}
