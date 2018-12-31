/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.utils

import com.adamratzman.spotify.main.SpotifyScope
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val access_token: String, val token_type: String, val expires_in: Int,
    @Optional val refresh_token: String? = null, val scope: String?
) {
    fun getScopes(): List<SpotifyScope> {
        val scopes = mutableListOf<SpotifyScope>()
        scope?.split(" ")?.forEach { split ->
            SpotifyScope.values().forEach { if (split == it.uri) scopes.add(it) }
        }
        return scopes
    }
}

@Serializable
data class ErrorResponse(val error: ErrorObject)

data class ErrorObject(val status: Int, val message: String)

class SpotifyUriException(message: String) : BadRequestException(message)

open class BadRequestException(message: String) : Exception(message) {
    constructor(error: ErrorObject) : this("Received Status Code ${error.status}. Error cause: ${error.message}")
}