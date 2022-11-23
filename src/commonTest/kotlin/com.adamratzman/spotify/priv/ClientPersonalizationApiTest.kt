/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.priv

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.endpoints.client.ClientPersonalizationApi
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.adamratzman.spotify.runTestOnDefaultDispatcher

class ClientPersonalizationApiTest : AbstractTest<SpotifyClientApi>() {
    @Test
    fun testGetTopArtists() = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>()
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        assertTrue(
            api.personalization.getTopArtists(
                5,
                timeRange = ClientPersonalizationApi.TimeRange.MEDIUM_TERM
            ).items.isNotEmpty()
        )
    }

    @Test
    fun testGetTopTracks() = runTestOnDefaultDispatcher {
        buildApi<SpotifyClientApi>()
        if (!isApiInitialized()) return@runTestOnDefaultDispatcher

        assertTrue(api.personalization.getTopTracks(5).items.isNotEmpty())
    }
}
