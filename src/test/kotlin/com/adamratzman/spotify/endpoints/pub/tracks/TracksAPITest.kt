package com.adamratzman.spotify.kotlin.endpoints.pub.tracks

import junit.framework.TestCase
import com.adamratzman.spotify.main.api
import com.adamratzman.spotify.main.gson

class TracksAPITest : TestCase() {
    fun testGetTracks() {
        println(api.tracks.getTracks(null, "7cU84qjHFxJ39COxWltHyY", "4o4sj7dVrT51NKMyeG8T5y"))
    }

    fun testGetAudioAnalysis() {
        println(gson.toJson(api.tracks.getAudioAnalysis("4o4sj7dVrT51NKMyeG8T5y")))
        println(api.tracks.getAudioAnalysis("7cU84qjHFxJ39COxWltHyY"))
    }

    fun testGetTrack() {
        println(api.tracks.getTrack("7cQMJ0wDqVS5iM0JIvP6Ay"))
    }

    fun testGetAudioFeatures() {
        println(api.tracks.getAudioFeatures("7cU84qjHFxJ39COxWltHyY", "4o4sj7dVrT51NKMyeG8T5y"))
    }

}