/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package private

import com.adamratzman.spotify.SpotifyApi.Companion.spotifyClientApi

fun main() {
    // instantiate api
    val api = spotifyClientApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET"),
            System.getenv("SPOTIFY_REDIRECT_URI")) {
        authorization {
            tokenString = System.getenv("SPOTIFY_TOKEN_STRING")
        }
    }.build()

}
