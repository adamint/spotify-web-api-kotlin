package com.adamratzman.spotify.kotlin.endpoints.priv.personalization

import clientApi
import org.junit.Test

internal class PersonalizationAPITest {
    @Test
    fun getTopArtists() {
        println(clientApi.personalization.getTopArtists().complete())
    }

    @Test
    fun getTopTracks() {
        println(clientApi.personalization.getTopTracks().complete())
    }

}