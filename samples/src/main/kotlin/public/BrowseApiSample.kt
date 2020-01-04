package public

import com.adamratzman.spotify.spotifyAppApi

fun main() {
    val api = spotifyAppApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET")
    )
}