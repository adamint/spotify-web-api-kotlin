/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamratzman.spotify.pub

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.runTestOnDefaultDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestResult
import kotlin.test.Test
import kotlin.test.assertTrue

class MarketsApiTest : AbstractTest<GenericSpotifyApi>() {
    @Test
    fun testGetAvailableMarkets(): TestResult = runTestOnDefaultDispatcher {
        buildApi(::testGetAvailableMarkets.name)
        assertTrue(api.markets.getAvailableMarkets().isNotEmpty())
    }
}
