/* Spotify Web API - Kotlin Wrapper; MIT License, 2019; Original author: Adam Ratzman */
package com.adamratzman.spotify

fun main(args: Array<String>) {
    val _clientId = args[0]
    val _clientSecret = args[1]
    val _redirectUri = args[2]

    val _authorizationCode = args.getOrNull(3)

    if (args.size < 4) {
        val authorizationUrl = spotifyClientApi {
            credentials {
                clientId = _clientId
                clientSecret = _clientSecret
                redirectUri = _redirectUri
            }
        }.getAuthorizationUrl(*SpotifyScope.values())

        println(authorizationUrl)
    } else {
        val token = spotifyClientApi {
            credentials {
                clientId = _clientId
                clientSecret = _clientSecret
                redirectUri = _redirectUri
            }
            authorization {
                authorizationCode = _authorizationCode!!
            }
        }.build().token

        println(token.accessToken)
        println(token.scopes)
        print(token.refreshToken)
    }
}
