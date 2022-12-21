/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.models.AuthenticationError
import com.adamratzman.spotify.models.ErrorObject
import io.ktor.client.plugins.ResponseException

public sealed class SpotifyException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    public abstract class UnNullableException(message: String) : SpotifyException(message)

    /**
     * Thrown when a request fails.
     *
     * @param statusCode The status code of the request, if this exception is thrown after the completion of an HTTP request.
     * @param reason The reason for the failure, as a readable message.
     */
    public open class BadRequestException(
        message: String,
        public val statusCode: Int? = null,
        public val reason: String? = null,
        cause: Throwable? = null
    ) :
        SpotifyException(message, cause) {
        public constructor(message: String, cause: Throwable? = null) : this(message, null, null, cause)
        public constructor(error: ErrorObject?, cause: Throwable? = null) : this(
            "Received Status Code ${error?.status}. Error cause: ${error?.message}" + (
                error?.reason?.let { ". Reason: ${error.reason}" }
                    ?: ""
                ),
            error?.status,
            error?.reason,
            cause
        )

        public constructor(authenticationError: AuthenticationError) :
            this(
                "Authentication error: ${authenticationError.error}. Description: ${authenticationError.description}",
                401
            )

        public constructor(responseException: ResponseException) :
            this(
                responseException.message ?: "Bad Request",
                responseException.response.status.value,
                null,
                responseException
            )
    }

    /**
     * Exception signifying that JSON (de)serialization failed. This is likely a library error rather than user error.
     */
    public class ParseException(message: String, cause: Throwable? = null) : SpotifyException(message, cause)

    /**
     * Exception signifying that authentication (via token or code) was unsuccessful, likely due to an invalid access token, code,
     * or refresh token.
     */
    public class AuthenticationException(message: String, cause: Throwable? = null) : SpotifyException(message, cause) {
        public constructor(authenticationError: AuthenticationError?) :
            this("Authentication error: ${authenticationError?.error}. Description: ${authenticationError?.description}")
    }

    /**
     * Exception signifying that the HTTP request associated with this API endpoint timed out (by default, after 100 seconds).
     */
    public class TimeoutException(message: String, cause: Throwable? = null) : SpotifyException(message, cause)

    /**
     * Exception signifying that re-authentication via spotify-auth is necessary. Thrown by default when refreshTokenProducer is null.
     */
    public class ReAuthenticationNeededException(cause: Throwable? = null, message: String? = null) :
        SpotifyException(message ?: "Re-authentication is needed.", cause)

    /**
     * Exception signifying that the current api token does not have the necessary scope to complete this request
     *
     */
    public class SpotifyScopesNeededException(cause: Throwable? = null, public val missingScopes: List<SpotifyScope>) :
        BadRequestException(
            cause = cause,
            message = "You tried to call a method that requires the following missing scopes: $missingScopes. Please make sure that your token is requested with these scopes."
        )
}
