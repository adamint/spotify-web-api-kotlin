/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

val api = when {
    System.getProperty("spotifyRedirectUri") == null -> {
        spotifyAppApi {
            credentials {
                clientId = System.getProperty("clientId")
                clientSecret = System.getProperty("clientSecret")
            }
        }.build()
    }
    else -> {
        spotifyClientApi {
            credentials {
                clientId = System.getProperty("clientId")
                clientSecret = System.getProperty("clientSecret")
                redirectUri = System.getProperty("spotifyRedirectUri")
            }
            authorization {
                tokenString = System.getProperty("spotifyTokenString")
            }
        }.build()
    }
}