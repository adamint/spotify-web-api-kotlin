/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.endpoints.client.ClientPersonalizationApi
import com.adamratzman.spotify.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ClientPersonalizationApiTest {
    lateinit var api: SpotifyClientApi

    init {
        runBlockingTest {
            (buildSpotifyApi() as? SpotifyClientApi)?.let { api = it }
        }
    }

    fun testPrereq() = ::api.isInitialized

    @Test
    fun testGetTopArtists() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertTrue(
                api.personalization
                    .getTopArtists(5, timeRange = ClientPersonalizationApi.TimeRange.MEDIUM_TERM)
                    .items
                    .isNotEmpty()
            )
        }
    }

    @Test
    fun testGetTopTracks() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertTrue(api.personalization.getTopTracks(5).items.isNotEmpty())
        }
    }
}
