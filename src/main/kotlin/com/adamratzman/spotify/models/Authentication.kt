/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify.models

import com.adamratzman.spotify.SpotifyScope
import com.beust.klaxon.Json

/**
 * Represents a Spotify Token, retrieved through instantiating a new [SpotifyAPI]
 */
data class Token(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "expires_in")val expiresIn: Int,
    @Json(name = "refresh_token") val refreshToken: String? = null,
    @Json(ignored = false) private val scopeString: String? = null,
    @Json(ignored = true) val scopes: List<SpotifyScope>? = scopeString?.let { str ->
        str.split(" ").mapNotNull { scope -> SpotifyScope.values().find { it.uri == scope } }
    }
)