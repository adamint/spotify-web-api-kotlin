/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.endpoints.client.ClientPersonalizationApi
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.spotifyApi
import kotlin.test.Test
import kotlin.test.assertTrue

class ClientPersonalizationApiTest {
    lateinit var api: SpotifyClientApi

    private suspend fun testPrereq(): Boolean {
        spotifyApi.await()?.let { it as? SpotifyClientApi }?.let { api = it }
        return ::api.isInitialized
    }

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
