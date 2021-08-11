/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
package com.adamratzman.spotify.priv

import com.adamratzman.spotify.AbstractTest
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException.BadRequestException
import com.adamratzman.spotify.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ClientEpisodeApiTest : AbstractTest<SpotifyClientApi>() {
    @Test
    fun testGetEpisode() {
        return runBlockingTest {
            super.build<SpotifyClientApi>()
            if (!testPrereq()) return@runBlockingTest else api

            assertNull(api!!.episodes.getEpisode("nonexistant episode"))
            assertNotNull(api!!.episodes.getEpisode("3lMZTE81Pbrp0U12WZe27l"))
        }
    }

    @Test
    fun testGetEpisodes() {
        return runBlockingTest {
            super.build<SpotifyClientApi>()
            if (!testPrereq()) return@runBlockingTest else api!!

            assertFailsWith<BadRequestException> { api!!.episodes.getEpisodes("hi", "dad") }
            assertFailsWith<BadRequestException> {
                api!!.episodes.getEpisodes("1cfOhXP4GQCd5ZFHoSF8gg", "j").map { it?.name }
            }
            assertEquals(
                listOf("The Great Inflation (Classic)"),
                api!!.episodes.getEpisodes("3lMZTE81Pbrp0U12WZe27l").map { it?.name }
            )
        }
    }
}
