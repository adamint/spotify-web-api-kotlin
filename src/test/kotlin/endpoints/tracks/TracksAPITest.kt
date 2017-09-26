package endpoints.tracks

import junit.framework.TestCase
import main.SpotifyAPI

class TracksAPITest : TestCase() {
    val api = SpotifyAPI.Builder().build()

    fun testGetTracks() {
        println(api.tracks.getTracks(null, "7cU84qjHFxJ39COxWltHyY", "4o4sj7dVrT51NKMyeG8T5y"))
    }

    fun testGetAudioAnalysis() {
        println(api.tracks.getAudioAnalysis("4o4sj7dVrT51NKMyeG8T5y"))
        println(api.tracks.getAudioAnalysis("7cU84qjHFxJ39COxWltHyY"))
    }

    fun testGetTrack() {
        println(api.tracks.getTrack("4o4sj7dVrT51NKMyeG8T5y"))
    }

    fun testGetAudioFeatures() {
        println(api.tracks.getAudioFeatures("7cU84qjHFxJ39COxWltHyY", "4o4sj7dVrT51NKMyeG8T5y"))
    }

}