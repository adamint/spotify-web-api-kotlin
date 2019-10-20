/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

val api = when {
    getEnvironmentalVariable("spotifyRedirectUri") == null -> {
        spotifyAppApi {
            credentials {
                clientId = getEnvironmentalVariable("clientId")
                clientSecret = getEnvironmentalVariable("clientSecret")
            }
        }.build()
    }
    else -> {
        spotifyClientApi {
            credentials {
                clientId = getEnvironmentalVariable("clientId")
                clientSecret = getEnvironmentalVariable("clientSecret")
                redirectUri = getEnvironmentalVariable("spotifyRedirectUri")
            }
            authorization {
                tokenString = getEnvironmentalVariable("spotifyTokenString")
            }
        }.build()
    }
}

expect fun getEnvironmentalVariable(name: String): String?