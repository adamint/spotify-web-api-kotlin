/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package public

import com.adamratzman.spotify.SpotifyApi.Companion.spotifyAppApi

fun main() {
    // instantiate api
    val api = spotifyAppApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET")
    ).build()

    // get profile for adamratzman1
    println(api.users.getProfile("adamratzman1").complete())


    // get profile of non-existant user
    println(api.users.getProfile("nonexistantuserjjjjjjjjjjj"))
}
