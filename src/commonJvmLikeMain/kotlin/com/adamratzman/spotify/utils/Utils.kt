package com.adamratzman.spotify.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * The current time in milliseconds since UNIX epoch.
 */
actual fun getCurrentTimeMs(): Long = Clock.System.now().toEpochMilliseconds()

/**
 * Format date to ISO 8601 format
 */
internal actual fun formatDate(date: Long): String {
    return Instant.fromEpochMilliseconds(date).toString()
}