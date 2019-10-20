/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyAPI
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.getCurrentTimeMs
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Represents an identifiable Spotify object such as an Album or Recommendation Seed
 */
abstract class Identifiable(
    href: String?,
    @Transient override val id: String
) : IdentifiableNullable(href, id)

/**
 * Represents an identifiable Spotify object such as an Album or Recommendation Seed
 *
 * @property href A link to the Spotify web api endpoint associated with this request
 * @property id The Spotify id of the associated object
 */
abstract class IdentifiableNullable(
    @Transient val href: String?,
    @Transient open val id: String?
) : NeedsApi()

/**
 * Represents a core Spotify object such as a Track or Album
 *
 * @property uri The URI associated with the object
 * @property externalUrls Known external URLs for this object
 */
abstract class CoreObject(
    _href: String,
    _id: String,
    @Transient val uri: SpotifyUri,
    _externalUrls: Map<String, String>
) : Identifiable(_href, _id) {
    @Transient
    val externalUrls: List<ExternalUrl> = _externalUrls.map { ExternalUrl(it.key, it.value) }
}

abstract class RelinkingAvailableResponse(
    @Transient val linkedTrack: LinkedTrack? = null,
    _href: String,
    _id: String,
    _uri: SpotifyUri,
    _externalUrls: Map<String, String>
) : CoreObject(_href, _id, _uri, _externalUrls) {
    fun isRelinked() = linkedTrack != null
}

class ExternalUrl(val name: String, val url: String)

/**
 * An external id linked to the result object
 *
 * @property key The identifier type, for example:
- "isrc" - International Standard Recording Code
- "ean" - International Article Number
- "upc" - Universal Product Code
 * @property id An external identifier for the object.
 */
class ExternalId(val key: String, val id: String)

/**
 * Provide access to the underlying [SpotifyAPI]
 *
 * @property api The API client associated with the request
 */
abstract class NeedsApi {
    @Transient
    lateinit var api: SpotifyAPI
}

interface ResultEnum {
    fun retrieveIdentifier(): Any
}

/**
 * Wraps around [ErrorObject]
 */
@Serializable
data class ErrorResponse(val error: ErrorObject, @Transient val exception: Exception? = null)

/**
 * An endpoint exception from Spotify
 *
 * @property status The HTTP status code
 * @property message A short description of the cause of the error.
 */
@Serializable
data class ErrorObject(val status: Int, val message: String)

class SpotifyAuthenticationException(message: String) : Exception(message)

data class AuthenticationError(
    val error: String,
    @SerialName("error_description") val description: String
)

class SpotifyUriException(message: String) : BadRequestException(message)

/**
 * Thrown when [SpotifyAPI.retryWhenRateLimited] is false and requests have been ratelimited
 *
 * @param time the time, in seconds, until the next request can be sent
 */
class SpotifyRatelimitedException(time: Long) :
    UnNullableException("Calls to the Spotify API have been ratelimited for $time seconds until ${getCurrentTimeMs() + time * 1000}ms")

abstract class UnNullableException(message: String) : SpotifyException(message)

/**
 * Thrown when a request fails
 */
open class BadRequestException(message: String, val statusCode: Int? = null, cause: Throwable? = null) :
    SpotifyException(message, cause) {
    constructor(error: ErrorObject, cause: Throwable? = null) : this(
        "Received Status Code ${error.status}. Error cause: ${error.message}",
        error.status,
        cause
    )

    constructor(authenticationError: AuthenticationError) :
            this(
                "Authentication error: ${authenticationError.error}. Description: ${authenticationError.description}",
                401
            )
}

typealias CountryCode = Market