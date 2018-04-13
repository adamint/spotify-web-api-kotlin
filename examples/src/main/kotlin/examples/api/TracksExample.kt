package examples.api

import api
import com.adamratzman.spotify.utils.BadRequestException

fun getAudioAnalysisBlocking() {
    try {
        val analysis = api.tracks.getAudioAnalysis("4o4sj7dVrT51NKMyeG8T5y").complete()
        println(analysis.bars)
    } catch (e: Exception) {
        println("Oh no! Error: ${e.printStackTrace()}")
    }

}

fun getTrackAsync() {
    try {
        api.tracks.getTrack("7cQMJ0wDqVS5iM0JIvP6Ay").queue {
            println("${it.name} by ${it.artists.map { it.name }}")
        }
    } catch (e: BadRequestException) {
        println("Oh no! Error: ${e.localizedMessage}")
    }
}