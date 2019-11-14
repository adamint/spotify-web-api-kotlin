/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.utils.getCurrentTimeMs
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Represents a Spotify Token, retrieved through instantiating a [SpotifyAPI]
 *
 * @property accessToken An access token that can be provided in subsequent calls,
 * for example to Spotify Web API services.
 * @property tokenType How the access token may be used: always “Bearer”.
 * @property expiresIn The time period (in seconds) for which the access token is valid.
 * @property refreshToken A token that can be sent to the Spotify Accounts service in place of an authorization code,
 * null if the token was created using a method that does not support token refresh
 * @property scopes A list of scopes granted access for this [accessToken]. An
 * empty list means that the token can only be used to acces public information.
 * @property expiresAt The time, in milliseconds, at which this Token expires
 */
@Serializable
data class Token(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("scope") private val scopeString: String? = null
) {
    @Transient
    val expiresAt: Long = getCurrentTimeMs() + expiresIn * 1000
    @Transient
    var scopes: List<SpotifyScope> = scopeString?.let { str ->
        str.split(" ").mapNotNull { scope -> SpotifyScope.values().find { it.uri.equals(scope, true) } }
    } ?: listOf()

    fun shouldRefresh(): Boolean = getCurrentTimeMs() > expiresAt
}

data class TokenValidityResponse(val isValid: Boolean, val exception: Exception?)
