package examples.api


import api
import com.adamratzman.spotify.utils.BadRequestException
import com.adamratzman.spotify.utils.Market

fun getArtistsBlocking() {
    try {
        val artists = api.artists.getArtists("this is an invalid artist id", "0C8ZW7ezQVs4URX5aX7Kqx").complete()
        println(artists)
    } catch (e: Exception) {
        println("Oh no! Error: ${e.printStackTrace()}")
    }

}

fun getTopTracksAsync() {
    try {
        api.artists.getArtistTopTracks("0C8ZW7ezQVs4URX5aX7Kqx", Market.US).queue { tracks ->
            println(tracks.map { "${it.name} (${it.duration_ms / 1000} seconds)" })
        }
    } catch (e: BadRequestException) {
        println("Oh no! Error: ${e.localizedMessage}")
    }
}