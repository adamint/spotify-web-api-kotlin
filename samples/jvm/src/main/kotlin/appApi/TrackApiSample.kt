/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package appApi

import com.adamratzman.spotify.spotifyAppApi

fun main() {
    // instantiate api
    val api = spotifyAppApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET")
    ).build()

    // get track "I'm Good" by the Mowgli's and check its popularity
    println(api.tracks.getTrack("spotify:track:5KQDGl3vAkNGyfvSbaW89E").complete()!!.popularity)

    // get track "I'm Good" and a non-existant track
    println(api.tracks.getTracks("spotify:track:5KQDGl3vAkNGyfvSbaW89E", "nonexistanttrack").complete())

    // get audio analysis for track "I'm Good"
    println(api.tracks.getAudioAnalysis("spotify:track:5KQDGl3vAkNGyfvSbaW89E").complete())

    // get audio features for track "I'm Good"
    println(api.tracks.getAudioFeatures("spotify:track:5KQDGl3vAkNGyfvSbaW89E").complete())

    // get audio features for an existing and non-existant track
    println(api.tracks.getAudioFeatures("spotify:track:5KQDGl3vAkNGyfvSbaW89E", "nonexistanttrack"))
}
