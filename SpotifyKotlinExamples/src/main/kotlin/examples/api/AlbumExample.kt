package examples.api

import api
import com.adamratzman.spotify.utils.BadRequestException
import java.util.concurrent.TimeUnit

fun getAlbumBlocking() {
    try {
        val album = api.albums.getAlbum("0dzeoQhVNzKkwM5ieOJC54").complete()
        (album.artists.map { it.name })
    } catch (e: BadRequestException) {
        println("Oh no! Error: ${e.localizedMessage}")
    }
}

fun getAlbumTracksAsync() {
    try {
        api.albums.getAlbumTracks("7rDvst38yYrJFGqs4W25Y8").queueAfter(2, TimeUnit.SECONDS, { tracks ->
            println(tracks.items.map { it.name })
        })
    } catch (e: BadRequestException) {
        println("Oh no! Error: ${e.localizedMessage}")
    }
}