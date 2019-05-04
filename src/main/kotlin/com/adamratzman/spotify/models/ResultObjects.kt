/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.main.SpotifyAPI
import com.beust.klaxon.Json

/**
 * Allow for track relinking
 */
abstract class Linkable {
    @Json(ignored = true)
    lateinit var api: SpotifyAPI
}

interface ResultEnum {
    fun retrieveIdentifier(): Any
}

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