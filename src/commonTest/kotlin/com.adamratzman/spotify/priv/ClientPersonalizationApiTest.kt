/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.endpoints.client.ClientPersonalizationApi
import com.adamratzman.spotify.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ClientPersonalizationApiTest {
    var api: SpotifyClientApi? = null

    init {
        runBlockingTest {
            (buildSpotifyApi() as? SpotifyClientApi)?.let { api = it }
        }
    }

    fun testPrereq() = api != null

    @Test
    fun testGetTopArtists() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest else api!!

            assertTrue(
                api!!.personalization
                    .getTopArtists(5, timeRange = ClientPersonalizationApi.TimeRange.MEDIUM_TERM)
                    .items
                    .isNotEmpty()
            )
        }
    }

    @Test
    fun testGetTopTracks() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest else api!!

            assertTrue(api!!.personalization.getTopTracks(5).items.isNotEmpty())
        }
    }
}
