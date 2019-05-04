/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.utils

import com.adamratzman.spotify.models.BadRequestException
import com.adamratzman.spotify.models.ResultEnum

internal fun <T> catch(function: () -> T): T? {
    return try {
        function()
    } catch (e: BadRequestException) {
        null
    }
}


fun <T : ResultEnum> Array<T>.match(identifier: String)
        = firstOrNull { it.retrieveIdentifier().toString().equals(identifier, true) }