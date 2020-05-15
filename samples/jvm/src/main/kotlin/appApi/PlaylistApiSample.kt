/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package appApi

import com.adamratzman.spotify.models.Track
import com.adamratzman.spotify.spotifyAppApi
import com.adamratzman.spotify.utils.Market

fun main() {
    // instantiate api
    val api = spotifyAppApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET")
    ).build()

    // get all playlists followed/owned by adamratzman1
    println(api.playlists.getUserPlaylists("adamratzman1").getAllItems().complete().filterNotNull().map { it.name })

    // get playlist "Hit Rewind"'s description in Andorra's market
    println(api.playlists.getPlaylist("spotify:playlist:37i9dQZF1DX0s5kDXi1oC5", Market.AD).complete()!!.description)

    // get up to 20 tracks for playlist "Hit Rewind"
    println(api.playlists.getPlaylistTracks("spotify:playlist:37i9dQZF1DX0s5kDXi1oC5").complete().items.map { (it.track as Track).name })

    // get playlist covers for "Hit Rewind"
    println(api.playlists.getPlaylistCovers("spotify:playlist:37i9dQZF1DX0s5kDXi1oC5").complete())
}
