/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyAPI
import com.beust.klaxon.Json

/**
 * Allow for track relinking
 *
 * @property api The API client used to create the object
 */
abstract class Linkable {
    @Json(ignored = true)
    lateinit var api: SpotifyAPI
}

interface ResultEnum {
    fun retrieveIdentifier(): Any
}

/**
 * Wraps around [ErrorObject]
 */
data class ErrorResponse(val error: ErrorObject)

/**
 * An endpoint exception from Spotify
 *
 * @property status The HTTP status code
 * @property message A short description of the cause of the error.
 */
data class ErrorObject(val status: Int, val message: String)

class SpotifyUriException(message: String) : BadRequestException(message)

/**
 * Thrown when a request fails
 */
open class BadRequestException(message: String) : Exception(message) {
    constructor(error: ErrorObject) : this("Received Status Code ${error.status}. Error cause: ${error.message}")
}