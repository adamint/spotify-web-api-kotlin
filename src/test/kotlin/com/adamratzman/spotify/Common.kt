/* Created by Adam Ratzman (2018) */
package com.adamratzman.spotify

import com.adamratzman.spotify.main.spotifyApi

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
            clientAuthentication {
                tokenString = System.getProperty("spotifyTokenString")
            }
        }.buildClient()
    }
}