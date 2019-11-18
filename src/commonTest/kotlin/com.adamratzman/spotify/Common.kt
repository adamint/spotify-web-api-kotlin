/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

val api = when {
    getEnvironmentVariable("spotifyRedirectUri")?.isNotBlank() != true -> {
        println("CLIENT ID: ${getEnvironmentVariable("clientId")}")
        println("CLIENT SECRET: ${getEnvironmentVariable("clientSecret")}")
        spotifyAppApi {
            credentials {
                clientId = getEnvironmentVariable("clientId")
                clientSecret = getEnvironmentVariable("clientSecret")
            }
        }.build()
    }
    else -> {
        spotifyClientApi {
            credentials {
                clientId = getEnvironmentVariable("clientId")
                clientSecret = getEnvironmentVariable("clientSecret")
                redirectUri = getEnvironmentVariable("spotifyRedirectUri")
            }
            authorization {
                tokenString = getEnvironmentVariable("spotifyTokenString")
            }
        }.build()
    }
}

expect fun getEnvironmentVariable(name: String): String?
