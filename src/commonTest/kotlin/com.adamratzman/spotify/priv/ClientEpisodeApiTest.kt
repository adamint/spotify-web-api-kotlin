/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.buildSpotifyApi
import com.adamratzman.spotify.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ClientEpisodeApiTest {
    lateinit var api: SpotifyClientApi

    init {
        runBlockingTest {
            (buildSpotifyApi() as? SpotifyClientApi)?.let { api = it }
        }
    }

    fun testPrereq() = ::api.isInitialized

    @Test
    fun testGetEpisode() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertNull(api.episodes.getEpisode("nonexistant episode"))
            assertNotNull(api.episodes.getEpisode("4IhgnOc8rwMW70agMWVVfh"))
        }
    }

    @Test
    fun testGetEpisodes() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertFailsWithSuspend<BadRequestException> { api.episodes.getEpisodes("hi", "dad") }
            assertFailsWithSuspend<BadRequestException> {
                api.episodes.getEpisodes("1cfOhXP4GQCd5ZFHoSF8gg", "j").map { it?.name }
            }
            assertEquals(
                listOf("The 'Murder Hornets' And The Honey Bees"),
                api.episodes.getEpisodes("4IhgnOc8rwMW70agMWVVfh").map { it?.name }
            )
        }
    }
}
