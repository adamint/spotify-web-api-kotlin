/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyScope
import com.beust.klaxon.Json

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
 */
data class Token(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "expires_in") val expiresIn: Int,
    @Json(name = "refresh_token") val refreshToken: String? = null,
    @Json(ignored = false, name="scope") private val scopeString: String? = null,
    @Json(ignored = true) val scopes: List<SpotifyScope> = scopeString?.let { str ->
        str.split(" ").mapNotNull { scope -> SpotifyScope.values().find { it.uri.equals(scope, true) } }
    } ?: listOf()
)