/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyScope
import com.squareup.moshi.Json

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
class TokenResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "expires_in") val expiresIn: Int,
    @Json(name = "refresh_token") val refreshToken: String?,
    @Json(name = "scope") private val scopeString: String?,
    @Transient val scopes: List<SpotifyScope>
)