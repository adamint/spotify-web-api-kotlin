package examples.api


import api
import com.adamratzman.spotify.utils.BadRequestException

fun getNewReleasesBlocking() {
    try {
        val releases = api.browse.getNewReleases().complete()
        println(releases.total)
    } catch (e: Exception) {
        println("Oh no! Error: ${e.printStackTrace()}")
    }

}

fun getRecommendationsAsync() {
    try {
        api.browse.getRecommendations(seedArtists = listOf("3TVXtAsR1Inumwj472S9r4"), seedGenres = listOf("pop", "country"), targets = hashMapOf(Pair("speechiness", 1.0), Pair("danceability", 1.0))).queue {
            println(it.tracks)
        }
    } catch (e: BadRequestException) {
        println("Oh no! Error: ${e.localizedMessage}")
    }
}