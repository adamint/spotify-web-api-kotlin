/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.priv

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.endpoints.client.ClientPersonalizationApi
import com.adamratzman.spotify.runTestOnDefaultDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestResult
import kotlin.test.Test
import kotlin.test.assertTrue

class ClientPersonalizationApiTest : AbstractTest<SpotifyClientApi>() {
    @Test
    fun testGetTopArtists(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testGetTopArtists.name)
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        assertTrue(
            api.personalization.getTopArtists(
                5,
                timeRange = ClientPersonalizationApi.TimeRange.MEDIUM_TERM
            ).items.isNotEmpty()
        )
    }

    @Test
    fun testGetTopTracks(): TestResult = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>(::testGetTopTracks.name)
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        assertTrue(api.personalization.getTopTracks(5).items.isNotEmpty())
    }
}
