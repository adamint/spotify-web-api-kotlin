package com.adamratzman.spotify.utils

import com.adamratzman.spotify.models.BadRequestException
import com.adamratzman.spotify.models.ResultEnum
import kotlinx.serialization.json.JsonElement

expect fun getCurrentTimeMs(): Long

internal fun jsonMap(vararg pairs: Pair<String, JsonElement>) = pairs.toMap().toMutableMap()

internal fun <T> catch(function: () -> T): T? {
    return try {
        function()
    } catch (e: BadRequestException) {
        if (e.statusCode !in listOf(400, 404)) throw e
        // we should only ignore the exception if it's 400 or 404. Otherwise, it's a larger issue
        null
    }
}

internal fun <T : ResultEnum> Array<T>.match(identifier: String) =
    firstOrNull { it.retrieveIdentifier().toString().equals(identifier, true) }