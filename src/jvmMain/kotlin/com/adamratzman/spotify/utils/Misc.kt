package com.adamratzman.spotify.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date

actual fun getCurrentTimeMs(): Long = System.currentTimeMillis()

internal actual fun formatDate(format: String, date: Long): String {
    return SimpleDateFormat(format).format(Date.from(Instant.ofEpochMilli(date)))
}