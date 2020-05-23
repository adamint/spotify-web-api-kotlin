/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.utils.Market
import com.adamratzman.spotify.utils.getCurrentTimeMs
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

internal const val TRANSIENT_EMPTY_STRING = ""

/**
 * Represents an identifiable Spotify object such as an Album or Recommendation Seed
 */
@Serializable
abstract class Identifiable : IdentifiableNullable() {
    abstract override val id: String
}

/**
 * Represents an identifiable Spotify object such as an Album or Recommendation Seed
 *
 * @property href A link to the Spotify web api endpoint associated with this request
 * @property id The Spotify id of the associated object
 */
@Serializable
abstract class IdentifiableNullable : NeedsApi() {
    abstract val href: String?
    abstract val id: String?
}

/**
 * Represents a core Spotify object such as a Track or Album
 *
 * @property uri The URI associated with the object
 * @property externalUrls Known external URLs for this object
 */
@Serializable
abstract class CoreObject : Identifiable() {
    protected abstract val externalUrlsString: Map<String, String>
    abstract override val href: String
    abstract val uri: SpotifyUri
    val externalUrls: List<ExternalUrl> get() = externalUrlsString.map { ExternalUrl(it.key, it.value) }
}

/**
 *
 * Represents a response for which a relinked track could be available
 *
 * @property linkedTrack Part of the response when Track Relinking is applied and is only part of the response
 * if the track linking, in fact, exists. The requested track has been replaced with a different track. The track contains information about the originally requested track.
 */
@Serializable
abstract class RelinkingAvailableResponse : CoreObject() {
    @SerialName("linked_from")
    abstract val linkedTrack: LinkedTrack?
    fun isRelinked() = linkedTrack != null
}

/**
 * Key/value pair mapping a name to an arbitrary url
 */
@Serializable
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
 * Provide access to the underlying [SpotifyApi]
 *
 * @property api The API client associated with the request
 */
@Serializable
abstract class NeedsApi {
    @Transient
    lateinit var api: GenericSpotifyApi
}

/**
 * Interface that allows easy identifier retrieval for children with an implemented identifier
 */
interface ResultEnum {
    fun retrieveIdentifier(): Any
}

/**
 * Wraps around [ErrorObject]. Serialized raw Spotify error response
 *
 * @property error The error code and message, as returned by Spotify
 * @property exception The associated Kotlin exception for this error
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
data class ErrorObject(val status: Int, val message: String, val reason: String? = null)

/**
 * An exception during the authentication process
 *
 * @property error Short error message
 * @property description More detailed description of the error
 */
@Serializable
data class AuthenticationError(
    val error: String,
    @SerialName("error_description") val description: String
)

/**
 * Thrown when [SpotifyApi.retryWhenRateLimited] is false and requests have been ratelimited
 *
 * @param time the time, in seconds, until the next request can be sent
 */
class SpotifyRatelimitedException(time: Long) :
        SpotifyException.UnNullableException("Calls to the Spotify API have been ratelimited for $time seconds until ${getCurrentTimeMs() + time * 1000}ms")

@Deprecated("Country enum has been updated to preserve consistency with Spotify documentation", ReplaceWith("Market"))
typealias CountryCode = Market
