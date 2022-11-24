/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.pub

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.GenericSpotifyApi
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.adamratzman.spotify.runTestOnDefaultDispatcher
import kotlinx.coroutines.test.TestResult

class MarketsApiTest : AbstractTest<GenericSpotifyApi>() {
    @Test
    fun testGetAvailableMarkets(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testGetAvailableMarkets.name)
        assertTrue(api.markets.getAvailableMarkets().isNotEmpty())
    }
}
