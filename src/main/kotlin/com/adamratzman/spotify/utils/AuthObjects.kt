/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.utils

import com.adamratzman.spotify.main.SpotifyScope
import com.beust.klaxon.Json

/**
 * Represents a Spotify Token, retrieved through instantiating a new [SpotifyAPI]
 */
data class Token(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "expires_in")val expiresIn: Int,
    @Json(name = "refresh_token") val refreshToken: String? = null,
    @Json(ignored = false) private val scopeString: String? = null,
    @Json(ignored = true) val scopes: List<SpotifyScope>? = scopeString?.let { str ->
        str.split(" ").mapNotNull { scope -> SpotifyScope.values().find { it.uri == scope } }
    }
)

/**
 * Wrapper around [ErrorObject]
 */
data class ErrorResponse(val error: ErrorObject)

/**
 * Contains a parsed error from Spotify
 *
 * @property status The HTTP status code
 * @property message A short description of the cause of the error.
 */
data class ErrorObject(val status: Int, val message: String)

class SpotifyUriException(message: String) : BadRequestException(message)

open class BadRequestException(message: String) : Exception(message) {
    constructor(error: ErrorObject) : this("Received Status Code ${error.status}. Error cause: ${error.message}")
}