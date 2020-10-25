/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyApi
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.utils.getCurrentTimeMs
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a Spotify Token, retrieved through instantiating a [SpotifyApi]
 *
 * @property accessToken An access token that can be provided in subsequent calls,
 * for example to Spotify Web API services.
 * @property tokenType How the access token may be used: always “Bearer”.
 * @property expiresIn The time period (in seconds) for which the access token is valid.
 * @property refreshToken A token that can be sent to the Spotify Accounts service in place of an authorization code,
 * null if the token was created using a method that does not support token refresh
 * @property scopes A list of scopes granted access for this [accessToken]. An
 * empty list means that the token can only be used to access public information.
 * @property expiresAt The time, in milliseconds, at which this Token expires
 */
@Serializable
public data class Token(
    @SerialName("access_token") var accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") var expiresIn: Int,
    @SerialName("refresh_token") var refreshToken: String? = null,
    @SerialName("scope") internal var scopeString: String? = null
) {
    val expiresAt: Long get() = getCurrentTimeMs() + expiresIn * 1000

    val scopes: List<SpotifyScope>? get() = scopeString?.let { str ->
        str.split(" ").mapNotNull { scope -> SpotifyScope.values().find { it.uri.equals(scope, true) } }
    }

    public fun shouldRefresh(): Boolean = getCurrentTimeMs() > expiresAt

    public companion object {
        public fun from(accessToken: String?, refreshToken: String?, scopes: List<SpotifyScope>, expiresIn: Int = 1): Token =
                Token(accessToken ?: "", "Bearer", expiresIn, refreshToken, scopes.joinToString(" ") { it.uri })
    }
}

public data class TokenValidityResponse(val isValid: Boolean, val exception: Exception?)
