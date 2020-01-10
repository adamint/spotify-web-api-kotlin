/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package public

import com.adamratzman.spotify.SpotifyApi.Companion.spotifyAppApi

fun main() {
    // instantiate api
    val api = spotifyAppApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET")
    ).build()

    // get album "Kids in Love" by the Mowgli's release date
    println(api.albums.getAlbum("spotify:album:4M2p2BIRHIeBu8Ew9IBQ0s").complete()!!.releaseDate)

    // get album "Kids in Love" and a non-existant album
    println(api.albums.getAlbums("spotify:album:4M2p2BIRHIeBu8Ew9IBQ0s", "nonexistantalbum"))

    // get album "Kids in Love"'s tracks
    println(api.albums.getAlbumTracks("spotify:album:4M2p2BIRHIeBu8Ew9IBQ0s").complete().map { it.name })
}
