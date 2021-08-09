/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertTrue

class MarketsApiTest : AbstractTest<GenericSpotifyApi>() {
    @Test
    fun testGetAvailableMarkets() {
        return runBlockingTest {
            super.build<GenericSpotifyApi>()
            if (!testPrereq()) return@runBlockingTest else api!!
            assertTrue(api!!.markets.getAvailableMarkets().isNotEmpty())
        }
    }
}
