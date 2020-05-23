/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package clientApi

import com.adamratzman.spotify.SpotifyUserAuthorization
import com.adamratzman.spotify.spotifyClientApi

fun main() {
    // instantiate api
    val api = spotifyClientApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET"),
            System.getenv("SPOTIFY_REDIRECT_URI"),
            SpotifyUserAuthorization(tokenString = System.getenv("SPOTIFY_TOKEN_STRING")))
            .build()

    // get and print your follower count and, if the associated scope has been authorized, the user's birthday
    val profile = api.users.getClientProfile().complete()

    println("${profile.followers.total} | ${profile.birthdate}")
}
