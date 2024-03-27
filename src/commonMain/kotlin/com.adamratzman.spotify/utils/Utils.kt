/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.models.ResultEnum
import kotlinx.serialization.json.JsonElement

/**
 * The current time in milliseconds since UNIX epoch.
 */
public expect fun getCurrentTimeMs(): Long

/**
 * Format date to ISO 8601 format
 */
internal expect fun formatDate(date: Long): String

internal fun jsonMap(vararg pairs: Pair<String, JsonElement>) = pairs.toMap().toMutableMap()

internal suspend inline fun <T> catch(catchInternalServerError: Boolean = false, crossinline function: suspend () -> T): T? {
    return try {
        function()
    } catch (e: SpotifyException.BadRequestException) {
        if (e.statusCode !in listOf(400, 404)) throw e
        else if (e.statusCode in 500..599 && catchInternalServerError) throw e
        
        // we should only ignore the exception if it's 400 or 404. Otherwise, it's a larger issue
        null
    }
}

internal fun <T : ResultEnum> Array<T>.match(identifier: String) =
    firstOrNull { it.retrieveIdentifier().toString().equals(identifier, true) }

public expect fun <T> runBlockingOnJvmAndNative(block: suspend () -> T): T
