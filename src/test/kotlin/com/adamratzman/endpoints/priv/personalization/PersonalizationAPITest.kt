package com.adamratzman.endpoints.priv.personalization

import com.adamratzman.main.clientApi
import org.junit.Test

internal class PersonalizationAPITest {
    @Test
    fun getTopArtists() {
        println(clientApi.personalization.getTopArtists())
    }

    @Test
    fun getTopTracks() {
        println(clientApi.personalization.getTopTracks())
    }

}