/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.models.AuthenticationError
import com.adamratzman.spotify.models.ErrorObject
import io.ktor.client.features.ResponseException

public sealed class SpotifyException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    public abstract class UnNullableException(message: String) : SpotifyException(message)

    /**
     * Thrown when a request fails
     */
    public open class BadRequestException(message: String, public val statusCode: Int? = null, public val reason: String? = null, cause: Throwable? = null) :
            SpotifyException(message, cause) {
        public constructor(message: String, cause: Throwable? = null) : this(message, null, null, cause)
        public constructor(error: ErrorObject?, cause: Throwable? = null) : this(
                "Received Status Code ${error?.status}. Error cause: ${error?.message}" + (error?.reason?.let { ". Reason: ${error.reason}" }
                        ?: ""),
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
                        responseException.response?.status?.value,
                        null,
                        responseException
                )
    }

    public class ParseException(message: String, cause: Throwable? = null) : SpotifyException(message, cause)

    public class AuthenticationException(message: String, cause: Throwable? = null) : SpotifyException(message, cause) {
        public constructor(authenticationError: AuthenticationError?) :
                this("Authentication error: ${authenticationError?.error}. Description: ${authenticationError?.description}")
    }

    public class TimeoutException(message: String, cause: Throwable? = null) : SpotifyException(message, cause)
}
