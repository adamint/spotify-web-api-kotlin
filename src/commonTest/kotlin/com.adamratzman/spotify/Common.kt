/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

import com.adamratzman.spotify.SpotifyApi.Companion.spotifyAppApi
import com.adamratzman.spotify.SpotifyApi.Companion.spotifyClientApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

val _clientId = getEnvironmentVariable("SPOTIFY_CLIENT_ID")
val _clientSecret = getEnvironmentVariable("SPOTIFY_CLIENT_SECRET")
val _redirectUri = getEnvironmentVariable("SPOTIFY_REDIRECT_URI")
val _tokenString = getEnvironmentVariable("SPOTIFY_TOKEN_STRING")

val api = when {
    _redirectUri?.isNotBlank() != true -> {
        spotifyAppApi {
            credentials {
                clientId = _clientId
                clientSecret = _clientSecret
            }
            options {
                json = Json(JsonConfiguration.Stable)
            }
        }.build()
    }
    else -> {
        spotifyClientApi {
            credentials {
                clientId = _clientId
                clientSecret = _clientSecret
                redirectUri = _redirectUri
            }
            authorization {
                tokenString = _tokenString
            }
            options {
                json = Json(JsonConfiguration.Stable)
            }
        }.build()
    }
}

expect fun getEnvironmentVariable(name: String): String?

expect fun Exception.stackTrace()

fun block(code: () -> Unit) = try {
    code
} catch (e: Exception) {
    e.stackTrace()
    throw e
}
