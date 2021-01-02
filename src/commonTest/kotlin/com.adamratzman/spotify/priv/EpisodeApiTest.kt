/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.assertFailsWithSuspend
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.spotifyApi
import com.adamratzman.spotify.utils.Market
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EpisodeApiTest {
    lateinit var api: SpotifyClientApi

    private suspend fun testPrereq(): Boolean {
        spotifyApi.await()?.let { it as? SpotifyClientApi }?.let { api = it }
        return ::api.isInitialized
    }

    @Test
    fun testGetEpisode() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertNull(api.episodes.getEpisode("nonexistant episode"))
        }
    }

    @Test
    fun testGetEpisodes() {
        runBlockingTest {
            if (!testPrereq()) return@runBlockingTest

            assertFailsWithSuspend<BadRequestException> { api.episodes.getEpisodes("hi", "dad", market = Market.US) }
            assertFailsWithSuspend<BadRequestException> { api.episodes.getEpisodes("1cfOhXP4GQCd5ZFHoSF8gg", "j").map { it?.name } }
            assertEquals(listOf("The 'Murder Hornets' And The Honey Bees"), api.episodes.getEpisodes("4IhgnOc8rwMW70agMWVVfh").map { it?.name })
        }
    }
}
