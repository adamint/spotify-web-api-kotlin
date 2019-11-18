/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

val api = when {
    getEnvironmentVariable("SPOTIFY_REDIRECT_URI")?.isNotBlank() != true -> {
        println("CLIENT ID: ${getEnvironmentVariable("SPOTIFY_CLIENT_ID")}")
        println("CLIENT SECRET: ${getEnvironmentVariable("SPOTIFY_CLIENT_SECRET")}")
        spotifyAppApi {
            credentials {
                clientId = getEnvironmentVariable("SPOTIFY_CLIENT_ID")
                clientSecret = getEnvironmentVariable("SPOTIFY_CLIENT_SECRET")
            }
        }.build()
    }
    else -> {
        println("CLIENT ID: ${getEnvironmentVariable("SPOTIFY_CLIENT_ID")}")
        println("CLIENT SECRET: ${getEnvironmentVariable("SPOTIFY_CLIENT_SECRET")}")
        println("REDIRECT URI: ${getEnvironmentVariable("SPOTIFY_REDIRECT_URI")}")
        println("TOKEN: ${getEnvironmentVariable("SPOTIFY_TOKEN_STRING")}")
        spotifyClientApi {
            credentials {
                clientId = getEnvironmentVariable("SPOTIFY_CLIENT_ID")
                clientSecret = getEnvironmentVariable("SPOTIFY_CLIENT_SECRET")
                redirectUri = getEnvironmentVariable("SPOTIFY_REDIRECT_URI")
            }
            authorization {
                tokenString = getEnvironmentVariable("SPOTIFY_TOKEN_STRING")
            }
        }.build()
    }
}

expect fun getEnvironmentVariable(name: String): String?
