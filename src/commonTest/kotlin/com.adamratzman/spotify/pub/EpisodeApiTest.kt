/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.utils.Market
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EpisodeApiTest {
    lateinit var api: GenericSpotifyApi

    val market = Market.US

    init {
        runBlockingTest {
            buildSpotifyApi()?.let { api = it }
        }
    }

    fun testPrereq() = ::api.isInitialized

    @Test
    fun testGetEpisode() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertNull(api.episodes.getEpisode("nonexistant episode", market = market))
            assertEquals("The 'Murder Hornets' And The Honey Bees", api.episodes.getEpisode("4IhgnOc8rwMW70agMWVVfh", market = market)?.name)
        }
    }

    @Test
    fun testGetEpisodes() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertFailsWithSuspend<BadRequestException> { api.episodes.getEpisodes("hi", "dad", market = market) }
            assertFailsWithSuspend<BadRequestException> {
                api.episodes.getEpisodes("1cfOhXP4GQCd5ZFHoSF8gg", "j", market = market).map { it?.name }
            }
            assertEquals(
                listOf("The 'Murder Hornets' And The Honey Bees"),
                api.episodes.getEpisodes("4IhgnOc8rwMW70agMWVVfh", market = market).map { it?.name }
            )
        }
    }
}
