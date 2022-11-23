/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify

actual fun getTestClientId(): String? = System.getenv("SPOTIFY_CLIENT_ID")
actual fun getTestClientSecret(): String? = System.getenv("SPOTIFY_CLIENT_SECRET")

actual fun arePlayerTestsEnabled(): Boolean {
    return System.getenv("SPOTIFY_ENABLE_PLAYER_TESTS")?.toBoolean() == true
}

actual fun areLivePkceTestsEnabled(): Boolean {
    return System.getenv("VERBOSE_TEST_ENABLED")?.toBoolean() ?: false
}

actual suspend fun buildSpotifyApi(): GenericSpotifyApi? {
    val clientId = getTestClientId()
    val clientSecret = getTestClientSecret()
    val redirectUri = System.getenv("SPOTIFY_REDIRECT_URI")
    val tokenString = System.getenv("SPOTIFY_TOKEN_STRING")
    val logHttp = System.getenv("SPOTIFY_LOG_HTTP")

    return when {
        tokenString?.isNotBlank() == true -> {
            spotifyClientApi {
                credentials {
                    this.clientId = clientId
                    this.clientSecret = clientSecret
                    this.redirectUri = redirectUri
                }
                authorization {
                    this.tokenString = tokenString
                }
            }.build()
        }
        clientId?.isNotBlank() == true -> {
            spotifyAppApi {
                credentials {
                    this.clientId = clientId
                    this.clientSecret = clientSecret
                }
            }.build()
        }
        else -> null
    }?.also { if (logHttp == "true") it.spotifyApiOptions.enableDebugMode = true }
}