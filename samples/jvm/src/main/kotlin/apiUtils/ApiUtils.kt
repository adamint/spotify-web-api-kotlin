/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package apiUtils

import com.adamratzman.spotify.spotifyAppApi
import kotlinx.coroutines.runBlocking

fun main() {
    // build sync
    spotifyAppApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET")
    ).build()

    // build ASYNC

    runBlocking {
        spotifyAppApi(
                System.getenv("SPOTIFY_CLIENT_ID"),
                System.getenv("SPOTIFY_CLIENT_SECRET")
        ).buildAsyncAt(this) { api ->
            // do stuff
        }
    }

    // build with options
    spotifyAppApi(
            System.getenv("SPOTIFY_CLIENT_ID"),
            System.getenv("SPOTIFY_CLIENT_SECRET")
    ) {
        options {
            automaticRefresh = false
            useCache = false
        }
    }.build()
}
