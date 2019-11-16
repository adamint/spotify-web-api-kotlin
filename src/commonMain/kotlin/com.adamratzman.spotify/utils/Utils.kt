/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.models.ResultEnum
import kotlinx.serialization.json.JsonElement

internal expect fun getCurrentTimeMs(): Long

internal fun jsonMap(vararg pairs: Pair<String, JsonElement>) = pairs.toMap().toMutableMap()

internal inline fun <T> catch(function: () -> T): T? {
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

internal expect fun <T> runBlocking(coroutineCode: suspend () -> T): T
