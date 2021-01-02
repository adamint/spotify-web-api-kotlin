/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.SpotifyApiOptions
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.utils.getCurrentTimeMs
import com.adamratzman.spotify.utils.getExternalUrls
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Represents an identifiable Spotify object such as an Album or Recommendation Seed
 */
@Serializable
public abstract class Identifiable : IdentifiableNullable() {
    abstract override val id: String
}

/**
 * Represents an identifiable Spotify object such as an Album or Recommendation Seed
 *
 * @property href A link to the Spotify web api endpoint associated with this request
 * @property id The Spotify id of the associated object
 */
@Serializable
public abstract class IdentifiableNullable : NeedsApi() {
    public abstract val href: String?
    public abstract val id: String?
}

/**
 * Represents a core Spotify object such as a Track or Album
 *
 * @property uri The URI associated with the object
 * @property externalUrls Known external URLs for this object
 */
@Serializable
public abstract class CoreObject : Identifiable() {
    protected abstract val externalUrlsString: Map<String, String>
    abstract override val href: String
    public abstract val uri: SpotifyUri
    public val externalUrls: List<ExternalUrl> get() = getExternalUrls(externalUrlsString)
}

/**
 *
 * Represents a response for which a relinked track could be available
 *
 * @property linkedTrack Part of the response when Track Relinking is applied and is only part of the response
 * if the track linking, in fact, exists. The requested track has been replaced with a different track. The track contains information about the originally requested track.
 */
@Serializable
public abstract class RelinkingAvailableResponse : CoreObject() {
    @SerialName("linked_from")
    public abstract val linkedTrack: LinkedTrack?

    /**
     * Check if this response has been relinked.
     */
    public fun isRelinked(): Boolean = linkedTrack != null
}

/**
 * Key/value pair mapping a name to an arbitrary url
 */
@Serializable
public class ExternalUrl(public val name: String, public val url: String)

/**
 * An external id linked to the result object
 *
 * @param key The identifier type, for example:
- "isrc" - International Standard Recording Code
- "ean" - International Article Number
- "upc" - Universal Product Code
 * @param id An external identifier for the object.
 */
public class ExternalId(public val key: String, public val id: String)

/**
 * Provide access to the underlying [SpotifyApi]
 *
 * @property api The API client associated with the request
 */
@Serializable
public abstract class NeedsApi {
    @Transient
    public lateinit var api: GenericSpotifyApi
}

/**
 * Interface that allows easy identifier retrieval for children with an implemented identifier
 */
public interface ResultEnum {
    public fun retrieveIdentifier(): Any
}

/**
 * Wraps around [ErrorObject]. Serialized raw Spotify error response
 *
 * @param error The error code and message, as returned by Spotify
 * @param exception The associated Kotlin exception for this error
 */
@Serializable
public data class ErrorResponse(val error: ErrorObject, @Transient val exception: Exception? = null)

/**
 * An endpoint exception from Spotify
 *
 * @param status The HTTP status code
 * @param message A short description of the cause of the error.
 */
@Serializable
public data class ErrorObject(val status: Int, val message: String, val reason: String? = null)

/**
 * An exception during the authentication process
 *
 * @param error Short error message
 * @param description More detailed description of the error
 */
@Serializable
public data class AuthenticationError(
    val error: String,
    @SerialName("error_description") val description: String
)

/**
 * Thrown when [SpotifyApiOptions.retryWhenRateLimited] is false and requests have been ratelimited
 *
 * @param time the time, in seconds, until the next request can be sent
 */
public class SpotifyRatelimitedException(time: Long) :
    SpotifyException.UnNullableException("Calls to the Spotify API have been ratelimited for $time seconds until ${getCurrentTimeMs() + time * 1000}ms")
