/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package appApi

import com.adamratzman.spotify.spotifyAppApi


fun main() {
    // instantiate api
    val api = spotifyAppApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET")
    ).build()

    // check if adamratzman and adamratzman1 are following "Hit Rewind"
    println(api.following.areFollowingPlaylist("spotify:playlist:37i9dQZF1DX0s5kDXi1oC5", "adamratzman", "adamratzman1"))

    // check if adamratzman1 is following "Hit Rewind"
    println(api.following.isFollowingPlaylist("spotify:playlist:37i9dQZF1DX0s5kDXi1oC5", "adamratzman1"))
}
