/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify

actual fun getTestClientId(): String? = System.getenv("SPOTIFY_CLIENT_ID")
actual fun getTestClientSecret(): String? = System.getenv("SPOTIFY_CLIENT_SECRET")
actual fun getTestRedirectUri(): String? = System.getenv("SPOTIFY_REDIRECT_URI")
actual fun getTestTokenString(): String? = System.getenv("SPOTIFY_TOKEN_STRING")
actual fun isHttpLoggingEnabled(): Boolean = System.getenv("SPOTIFY_LOG_HTTP") == "true"
actual fun arePlayerTestsEnabled(): Boolean = System.getenv("SPOTIFY_ENABLE_PLAYER_TESTS")?.toBoolean() == true
actual fun areLivePkceTestsEnabled(): Boolean = System.getenv("VERBOSE_TEST_ENABLED")?.toBoolean() ?: false

actual suspend fun buildSpotifyApi(): GenericSpotifyApi? {
    val clientId = getTestClientId()
    val clientSecret = getTestClientSecret()
    val tokenString = getTestTokenString()
    val logHttp = isHttpLoggingEnabled()
    var requestNumber = 0


    val optionsCreator: (SpotifyApiOptions.() -> Unit) = {
        this.enableDebugMode = logHttp
        this.enableLogger = true
        this.httpResponseSubscriber = { request, response ->
            /*println("=== request ${requestNumber} ===")
            println(request)
            println(response)*/
            requestNumber++
        }
    }

    return when {
        tokenString?.isNotBlank() == true -> {
            spotifyClientApi {
                credentials {
                    this.clientId = clientId
                    this.clientSecret = clientSecret
                    this.redirectUri = getTestRedirectUri()
                }
                authorization {
                    this.tokenString = tokenString
                }
                options(optionsCreator)
            }.build()
        }

        clientId?.isNotBlank() == true -> {
            spotifyAppApi {
                credentials {
                    this.clientId = clientId
                    this.clientSecret = clientSecret
                }
                options(optionsCreator)
            }.build()
        }

        else -> null
    }
}