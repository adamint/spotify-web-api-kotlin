/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.pub

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.GenericSpotifyApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.runBlockingTest
import com.adamratzman.spotify.utils.Market
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class EpisodeApiTest : AbstractTest<GenericSpotifyApi>() {
    val market = Market.US

    @Test
    fun testGetEpisode() {
        return runBlockingTest {
            super.build<GenericSpotifyApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertNull(api!!.episodes.getEpisode("nonexistant episode", market = market))
            assertEquals(
                "The Great Inflation (Classic)",
                api!!.episodes.getEpisode("3lMZTE81Pbrp0U12WZe27l", market = market)?.name
            )
            println("sample episode: ${api!!.episodes.getEpisode("3lMZTE81Pbrp0U12WZe27l", market)?.name}")
        }
    }

    @Test
    fun testGetEpisodes() {
        return runBlockingTest {
            super.build<GenericSpotifyApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertFailsWith<BadRequestException> { api!!.episodes.getEpisodes("hi", "dad", market = market) }
            assertFailsWith<BadRequestException> {
                api!!.episodes.getEpisodes("1cfOhXP4GQCd5ZFHoSF8gg", "j", market = market).map { it?.name }
            }
            assertEquals(
                listOf("The Great Inflation (Classic)"),
                api!!.episodes.getEpisodes("3lMZTE81Pbrp0U12WZe27l", market = market).map { it?.name }
            )
        }
    }
}
