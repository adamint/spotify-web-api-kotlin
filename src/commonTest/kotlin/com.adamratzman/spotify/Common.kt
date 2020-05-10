/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify

val _clientId = getEnvironmentVariable("SPOTIFY_CLIENT_ID")
val _clientSecret = getEnvironmentVariable("SPOTIFY_CLIENT_SECRET")
val _redirectUri = getEnvironmentVariable("SPOTIFY_REDIRECT_URI")
val _tokenString = getEnvironmentVariable("SPOTIFY_TOKEN_STRING")

val api = {
    println(_redirectUri)
    println(_clientId)
println(_redirectUri.isNullOrBlank())
    println(_clientId.isNullOrBlank())

}.let {  when {
    _redirectUri?.isNotBlank() != true -> {
        spotifyAppApi {
            credentials {
                clientId = _clientId
                clientSecret = _clientSecret
            }
        }.build()
    }
    _clientId?.isNotBlank() == true -> {
        spotifyClientApi {
            credentials {
                clientId = _clientId
                clientSecret = _clientSecret
                redirectUri = _redirectUri
            }
            authorization {
                tokenString = _tokenString
            }
        }.build()
    }
    else -> null
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
