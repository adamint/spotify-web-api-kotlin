/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.models.ResultEnum
import com.soywiz.klock.DateTime
import kotlinx.serialization.json.JsonElement

/**
 * The current time in milliseconds since UNIX epoch.
 */
public fun getCurrentTimeMs(): Long = DateTime.nowUnixLong()

internal fun jsonMap(vararg pairs: Pair<String, JsonElement>) = pairs.toMap().toMutableMap()

internal suspend inline fun <T> catch(crossinline function: suspend () -> T): T? {
    return try {
        function()
    } catch (e: SpotifyException.BadRequestException) {
        if (e.statusCode !in listOf(400, 404)) throw e
        // we should only ignore the exception if it's 400 or 404. Otherwise, it's a larger issue
        null
    }
}

internal fun <T : ResultEnum> Array<T>.match(identifier: String) =
    firstOrNull { it.retrieveIdentifier().toString().equals(identifier, true) }

internal expect fun formatDate(format: String, date: Long): String
