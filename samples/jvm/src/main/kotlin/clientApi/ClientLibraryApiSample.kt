/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package clientApi

import com.adamratzman.spotify.SpotifyUserAuthorization
import com.adamratzman.spotify.endpoints.client.LibraryType.TRACK
import com.adamratzman.spotify.spotifyClientApi

fun main() {
    // instantiate api
    val api = spotifyClientApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET"),
            System.getenv("SPOTIFY_REDIRECT_URI"),
            SpotifyUserAuthorization(tokenString = System.getenv("SPOTIFY_TOKEN_STRING")))
            .build()

    // get all your saved tracks
    println(api.library.getSavedTracks(limit = 50).getAllItems().complete().filterNotNull().map { it.track.name })

    // get your 11-20th saved albums
    println(api.library.getSavedAlbums(limit = 10, offset = 10).complete().filterNotNull().map { it.album.name })

    // check if your library contains the track "I'm Good" by the Mowgli's
    println(api.library.contains(TRACK, api.search.searchTrack("I'm Good the Mowgli's").complete()[0].id).complete())

    // add and remove track "Up" by Nav
    api.library.add(TRACK, "spotify:track:5qbcsZMwL0x46sX7VO37Ye").complete()
    api.library.remove(TRACK, "spotify:track:5qbcsZMwL0x46sX7VO37Ye").complete()
}
