package examples.api

import api
import com.adamratzman.spotify.utils.BadRequestException
import java.util.concurrent.TimeUnit

fun getPlaylistBlocking() {
    try {
        val playlist = api.playlists.getPlaylist("spotify", "59ZbFPES4DQwEjBpWHzrtC").complete()
        println(playlist.owner.id)
    } catch (e: Exception) {
        println("Oh no! Error: ${e.printStackTrace()}")
    }

}

fun getPlaylistCoversAsync() {
    try {
        api.playlists.getPlaylistCovers("spotify", "59ZbFPES4DQwEjBpWHzrtC").queueAfter(420, TimeUnit.MILLISECONDS, {
            println(it.map { it.url })
        })
    } catch (e: BadRequestException) {
        println("Oh no! Error: ${e.localizedMessage}")
    }
}