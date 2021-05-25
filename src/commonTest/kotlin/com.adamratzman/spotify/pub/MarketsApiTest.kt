/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertTrue

class MarketsApiTest {
    var api: GenericSpotifyApi? = null

    init {
        runBlockingTest {
            buildSpotifyApi()?.let { api = it }
        }
    }

    fun testPrereq() = api != null

    @Test
    fun testGetAvailableMarkets() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest else api!!
            assertTrue(api!!.markets.getAvailableMarkets().isNotEmpty())
        }
    }
}
