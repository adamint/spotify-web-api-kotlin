/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

val api = when {
    System.getProperty("spotifyRedirectUri") == null -> {
        spotifyApi {
            credentials {
                clientId = System.getProperty("clientId")
                clientSecret = System.getProperty("clientSecret")
            }
        }.buildCredentialed()
    }
    else -> {
        spotifyApi {
            credentials {
                clientId = System.getProperty("clientId")
                clientSecret = System.getProperty("clientSecret")
                redirectUri = System.getProperty("spotifyRedirectUri")
            }
            authentication {
                tokenString = System.getProperty("spotifyTokenString")
            }
        }.buildClient()
    }
}